package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.utilities.logging.Logged;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.dtos.eventDtos.DemandeDto;
import fr.fruityhedgeh0g.dtos.eventDtos.GroupRosterDto;
import fr.fruityhedgeh0g.dtos.eventDtos.MonGroupeDto;
import fr.fruityhedgeh0g.dtos.eventDtos.RegistrationDto;
import fr.fruityhedgeh0g.dtos.groupDtos.GroupRefDto;
import fr.fruityhedgeh0g.dtos.eventDtos.RosterDto;
import fr.fruityhedgeh0g.dtos.eventDtos.RosterEntryDto;
import fr.fruityhedgeh0g.entities.EventEntity;
import fr.fruityhedgeh0g.entities.EventRegistrationEntity;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.enums.EventStatusEnum;
import fr.fruityhedgeh0g.exceptions.ForbiddenActionException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.PhoneRequiredException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.EventRegistrationRepository;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalGroupService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalSectorService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Logged
@ApplicationScoped
@Default
public class EventServiceImpl implements EventService {

    @Inject
    EventRepository eventRepository;

    @Inject
    InternalSectorService internalSectorService;

    @Inject
    EventMapper eventMapper;

    @Inject
    EventRegistrationRepository registrationRepository;

    @Inject
    InternalUserService internalUserService;

    @Inject
    InternalGroupService internalGroupService;

    @Override
    public List<EventDto> listAll(boolean seesPlanification) {
        List<EventEntity> events = seesPlanification
                ? eventRepository.listAll()
                : eventRepository.listOutsidePlanification();
        return events.stream().map(eventMapper::toDto).toList();
    }

    @Override
    public EventDto getById(UUID eventId, boolean seesPlanification) {
        return eventMapper.toDto(
                eventRepository.findByIdOptional(eventId)
                        .filter(event -> seesPlanification || event.getStatus() != EventStatusEnum.PLANIFICATION)
                        .orElseThrow(() -> new UnknownResourceException("Event not found: " + eventId))
        );
    }

    @Override
    @Transactional
    public EventDto create(EventDto eventDto) {
        if (eventDto.getSectorId() == null)
            throw new InvalidResourceException("An Event belongs to a Secteur.");
        SectorEntity sector = internalSectorService.doGetEntityById(eventDto.getSectorId())
                .orElseThrow(() -> new UnknownResourceException("Sector not found: " + eventDto.getSectorId()));

        EventEntity event = eventMapper.toEntity(eventDto);
        event.setStatus(EventStatusEnum.PLANIFICATION);
        event.setSector(sector);
        normalizeMaximum(event);
        validate(event);
        eventRepository.persist(event);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventDto update(EventDto eventDto) {
        EventEntity event = eventOrThrow(eventDto.getEventId());
        refuseOnFinal(event);
        eventMapper.partialDtoToEntity(event, eventDto);
        normalizeMaximum(event);
        validate(event);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventDto changeStatus(UUID eventId, EventStatusEnum status) {
        EventEntity event = eventOrThrow(eventId);
        EventStatusEnum current = event.currentStatus();
        if (!current.canMoveTo(status))
            throw new InvalidResourceException("An Event cannot move from " + current.id() + " to " + status.id());
        event.setStatus(status);
        return eventMapper.toDto(event);
    }

    @Override
    @Transactional
    public RegistrationDto signUp(UUID eventId, UUID personId, UUID groupId) {
        EventEntity event = lockedEventOrThrow(eventId);
        UserEntity person = internalUserService.doGetEntityById(personId)
                .orElseThrow(() -> new UnknownResourceException("User not found: " + personId));

        Optional<EventRegistrationEntity> existing = registrationRepository.findByEventAndPerson(eventId, personId);
        if (existing.isPresent()) return toDto(existing.get());

        EventStatusEnum current = event.currentStatus();
        if (!current.acceptsSignUps())
            throw new InvalidResourceException("Sign-up is closed for an Event in " + current.id());
        if (!person.hasPhone())
            throw new PhoneRequiredException("A phone number is required to sign up: " + personId);

        EventRegistrationEntity registration = current == EventStatusEnum.OUVERT && placeLeft(event)
                ? registrationRepository.persistConfirmed(event, person)
                : registrationRepository.persistWaiting(event, person);
        if (groupId != null) registration.requestGroup(groupOrThrow(groupId));
        return toDto(registration);
    }

    @Override
    @Transactional
    public RegistrationDto requestGroup(UUID eventId, UUID personId, UUID groupId) {
        refuseOnArchived(eventOrThrow(eventId));
        EventRegistrationEntity registration = registrationOrThrow(eventId, personId);
        registration.requestGroup(groupOrThrow(groupId));
        return toDto(registration);
    }

    @Override
    @Transactional
    public RegistrationDto decideDemande(UUID eventId, UUID personId, Actor actor, boolean accept) {
        EventEntity event = lockedEventOrThrow(eventId);
        refuseOnArchived(event);
        EventRegistrationEntity registration = registrationOrThrow(eventId, personId);
        if (!registration.hasPendingDemande())
            throw new InvalidResourceException("No pending Demande de groupe for " + personId);
        requireLeaderOrBureau(actor, registration.getDemandeGroup());
        if (accept) {
            // Beyond the maximum the Demande stays pending, on the Groupe's Liste d'attente
            requireRoomIn(event, registration.getDemandeGroup());
            registration.acceptDemande();
        } else registration.refuseDemande();
        return toDto(registration);
    }

    @Override
    @Transactional
    public RosterDto placeInGroup(UUID eventId, UUID personId, UUID groupId) {
        EventEntity event = lockedEventOrThrow(eventId);
        refuseOnArchived(event);
        EventRegistrationEntity registration = registrationOrThrow(eventId, personId);
        if (registration.isWaiting())
            throw new InvalidResourceException("Only a Participant is placed in a Groupe.");
        GroupEntity group = groupOrThrow(groupId);
        if (!registration.ridesWith(group)) requireRoomIn(event, group);
        registration.placeIn(group);
        return rosterOf(event);
    }

    @Override
    @Transactional
    public RosterDto setGroupMaximum(UUID eventId, UUID groupId, Integer maximum) {
        EventEntity event = lockedEventOrThrow(eventId);
        refuseOnFinal(event);
        GroupEntity group = groupOrThrow(groupId);
        if (!event.isOfSectorOf(group))
            throw new InvalidResourceException("The Groupe " + groupId + " is not of the Event's Secteur.");
        // Lowering it below the current riders takes nobody out; it only stops new acceptances
        event.setMaximumOf(group, maximum);
        return rosterOf(event);
    }

    @Override
    @Transactional
    public RegistrationDto takeOutOfGroup(UUID eventId, UUID personId, Actor actor) {
        refuseOnArchived(eventOrThrow(eventId));
        EventRegistrationEntity registration = registrationOrThrow(eventId, personId);
        requireLeaderOrBureau(actor, registration.getGroup());
        registration.leaveGroup();
        return toDto(registration);
    }

    @Override
    public MonGroupeDto monGroupe(UUID chefId) {
        Optional<GroupEntity> led = internalGroupService.doGetEntityLedBy(chefId);
        if (led.isEmpty()) return new MonGroupeDto(null, List.of());

        GroupEntity group = led.get();
        Map<EventEntity, List<EventRegistrationEntity>> byEvent = registrationRepository.listByGroup(group.getGroupId()).stream()
                .collect(Collectors.groupingBy(EventRegistrationEntity::getEvent, LinkedHashMap::new, Collectors.toList()));
        // Every Event of the Groupe's Secteur a Chef prepares, even before anyone asks for the Groupe
        if (group.getSector() != null)
            eventRepository.list("sector.sectorId", group.getSector().getSectorId()).stream()
                    .filter(e -> e.currentStatus().acceptsSignUps() || e.currentStatus() == EventStatusEnum.EN_COURS)
                    .forEach(e -> byEvent.putIfAbsent(e, List.of()));

        List<MonGroupeDto.EventRoster> events = byEvent.entrySet().stream()
                .filter(e -> e.getKey().currentStatus() != EventStatusEnum.ARCHIVE)
                .sorted(Comparator.comparing((Map.Entry<EventEntity, List<EventRegistrationEntity>> e) -> e.getKey().getStartDateTime()))
                .map(e -> new MonGroupeDto.EventRoster(
                        e.getKey().getEventId(), e.getKey().getName(), e.getKey().getStartDateTime(), e.getKey().currentStatus(),
                        e.getKey().maximumOf(group),
                        e.getValue().stream().filter(r -> r.ridesWith(group)).map(EventServiceImpl::toRosterEntry).toList(),
                        e.getValue().stream().filter(r -> r.asksFor(group)).map(EventServiceImpl::toRosterEntry).toList()))
                .toList();
        return new MonGroupeDto(GroupRefDto.of(group), events);
    }

    /** A Chef acts only on the Groupe they lead through their Affectation; the Bureau on any Groupe. */
    private static void requireLeaderOrBureau(Actor actor, GroupEntity group) {
        boolean leads = group != null && group.getChef() != null && group.getChef().getUserId().equals(actor.personId());
        if (!actor.bureau() && !leads)
            throw new ForbiddenActionException(actor.personId() + " does not lead this Groupe.");
    }

    /** Refuses one more rider in the Groupe once its maximum at the Event is reached; no maximum means no limit. */
    private void requireRoomIn(EventEntity event, GroupEntity group) {
        Integer maximum = event.maximumOf(group);
        if (maximum != null && registrationRepository.countInGroup(event.getEventId(), group.getGroupId()) >= maximum)
            throw new InvalidResourceException("The Groupe is at its maximum of " + maximum + " at this Event.");
    }

    private GroupEntity groupOrThrow(UUID groupId) {
        return internalGroupService.doGetEntityById(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupId));
    }

    @Override
    @Transactional
    public void withdraw(UUID eventId, UUID personId) {
        EventEntity event = eventOrThrow(eventId);
        EventRegistrationEntity registration = registrationOrThrow(eventId, personId);
        refuseOnArchived(event);
        registrationRepository.delete(registration);
    }

    @Override
    public List<RegistrationDto> registrationsOf(UUID personId) {
        return registrationRepository.listByPerson(personId).stream().map(EventServiceImpl::toDto).toList();
    }

    @Override
    public RosterDto roster(UUID eventId) {
        return rosterOf(eventOrThrow(eventId));
    }

    @Override
    @Transactional
    public RosterDto promote(UUID eventId, UUID personId) {
        EventEntity event = lockedEventOrThrow(eventId);
        EventRegistrationEntity registration = registrationOrThrow(eventId, personId);
        refuseOnArchived(event);
        if (!registration.isWaiting())
            throw new InvalidResourceException(personId + " is already a Participant.");
        if (!placeLeft(event))
            throw new InvalidResourceException("The Event is at its maximum of " + event.getMaxParticipants() + " Participants.");
        registration.setWaiting(false);
        return rosterOf(event);
    }

    @Override
    @Transactional
    public RosterDto removeFromRoster(UUID eventId, UUID personId) {
        EventEntity event = eventOrThrow(eventId);
        EventRegistrationEntity registration = registrationOrThrow(eventId, personId);
        refuseOnArchived(event);
        registrationRepository.delete(registration);
        registrationRepository.flush();
        return rosterOf(event);
    }

    private RosterDto rosterOf(EventEntity event) {
        List<EventRegistrationEntity> all = registrationRepository.listByEvent(event.getEventId());
        List<GroupEntity> groups = event.getSector() == null ? List.of()
                : internalGroupService.doListEntitiesOfSector(event.getSector().getSectorId());
        return new RosterDto(
                event.getEventId(),
                event.getMaxParticipants(),
                all.stream().filter(r -> !r.isWaiting()).map(EventServiceImpl::toRosterEntry).toList(),
                all.stream().filter(EventRegistrationEntity::isWaiting).map(EventServiceImpl::toRosterEntry).toList(),
                groups.stream().map(group -> new GroupRosterDto(
                        GroupRefDto.of(group),
                        event.maximumOf(group),
                        all.stream().filter(r -> r.ridesWith(group)).map(EventServiceImpl::toRosterEntry).toList(),
                        all.stream().filter(r -> r.asksFor(group)).map(EventServiceImpl::toRosterEntry).toList()
                )).toList()
        );
    }

    private static RosterEntryDto toRosterEntry(EventRegistrationEntity registration) {
        UserEntity person = registration.getPerson();
        return new RosterEntryDto(person.getUserId(), person.getFirstName(), person.getLastName(), person.getPhone(),
                registration.getMode(), registration.getSignedUpAt(),
                GroupRefDto.of(registration.getGroup()), DemandeDto.of(registration));
    }

    /** true when the Event has no maximum or is still under it. */
    private boolean placeLeft(EventEntity event) {
        return event.getMaxParticipants() == null
                || registrationRepository.countParticipants(event.getEventId()) < event.getMaxParticipants();
    }

    private static void refuseOnFinal(EventEntity event) {
        if (event.currentStatus().isFinal())
            throw new InvalidResourceException("An archived or cancelled Event is no longer edited.");
    }

    private void refuseOnArchived(EventEntity event) {
        if (event.currentStatus() == EventStatusEnum.ARCHIVE)
            throw new InvalidResourceException("An archived Event keeps its roster.");
    }

    private EventRegistrationEntity registrationOrThrow(UUID eventId, UUID personId) {
        return registrationRepository.findByEventAndPerson(eventId, personId)
                .orElseThrow(() -> new UnknownResourceException("No sign-up of " + personId + " for " + eventId));
    }

    /** Locks the Event so concurrent sign-ups and moves up cannot both take the last place. */
    private EventEntity lockedEventOrThrow(UUID eventId) {
        return eventRepository.findByIdOptional(eventId, LockModeType.PESSIMISTIC_WRITE)
                .orElseThrow(() -> new UnknownResourceException("Event not found: " + eventId));
    }

    private static RegistrationDto toDto(EventRegistrationEntity registration) {
        return new RegistrationDto(registration.getEvent().getEventId(), registration.getMode(), registration.status(),
                registration.getSignedUpAt(), GroupRefDto.of(registration.getGroup()), DemandeDto.of(registration));
    }

    /** A maximum of 0 or less means no maximum. */
    private static void normalizeMaximum(EventEntity event) {
        if (event.getMaxParticipants() != null && event.getMaxParticipants() <= 0)
            event.setMaxParticipants(null);
    }

    private EventEntity eventOrThrow(UUID eventId) {
        if (eventId == null) throw new InvalidResourceException("Missing event id.");
        return eventRepository.findByIdOptional(eventId)
                .orElseThrow(() -> new UnknownResourceException("Event not found: " + eventId));
    }

    /** An Event has a name, a start and an end, in that order. */
    private static void validate(EventEntity event) {
        if (event.getName() == null || event.getName().isBlank())
            throw new InvalidResourceException("An Event has a name.");
        if (event.getStartDateTime() == null || event.getEndDateTime() == null)
            throw new InvalidResourceException("An Event has a start and an end.");
        if (event.getEndDateTime().isBefore(event.getStartDateTime()))
            throw new InvalidResourceException("An Event cannot end before it starts.");
    }
}
