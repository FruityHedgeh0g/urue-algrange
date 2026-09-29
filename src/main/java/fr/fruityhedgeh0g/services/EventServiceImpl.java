package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.utilities.logging.Logged;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.dtos.eventDtos.RegistrationDto;
import fr.fruityhedgeh0g.entities.EventEntity;
import fr.fruityhedgeh0g.entities.EventRegistrationEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.enums.EventStatusEnum;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.PhoneRequiredException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.EventRegistrationRepository;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalSectorService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
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
        if (event.currentStatus().isFinal())
            throw new InvalidResourceException("An archived or cancelled Event is no longer edited.");
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
    public RegistrationDto signUp(UUID eventId, UUID personId) {
        // Locks the Event so concurrent sign-ups cannot both take the last place
        EventEntity event = eventRepository.findByIdOptional(eventId, LockModeType.PESSIMISTIC_WRITE)
                .orElseThrow(() -> new UnknownResourceException("Event not found: " + eventId));
        UserEntity person = internalUserService.doGetEntityById(personId)
                .orElseThrow(() -> new UnknownResourceException("User not found: " + personId));

        Optional<EventRegistrationEntity> existing = registrationRepository.findByEventAndPerson(eventId, personId);
        if (existing.isPresent()) return toDto(existing.get());

        EventStatusEnum current = event.currentStatus();
        if (!current.acceptsSignUps())
            throw new InvalidResourceException("Sign-up is closed for an Event in " + current.id());
        if (!person.hasPhone())
            throw new PhoneRequiredException("A phone number is required to sign up: " + personId);

        boolean placeLeft = event.getMaxParticipants() == null
                || registrationRepository.countParticipants(eventId) < event.getMaxParticipants();
        EventRegistrationEntity registration = current == EventStatusEnum.OUVERT && placeLeft
                ? registrationRepository.persistConfirmed(event, person)
                : registrationRepository.persistWaiting(event, person);
        return toDto(registration);
    }

    @Override
    @Transactional
    public void withdraw(UUID eventId, UUID personId) {
        EventEntity event = eventOrThrow(eventId);
        EventRegistrationEntity registration = registrationRepository.findByEventAndPerson(eventId, personId)
                .orElseThrow(() -> new UnknownResourceException("No sign-up of " + personId + " for " + eventId));
        if (event.currentStatus() == EventStatusEnum.ARCHIVE)
            throw new InvalidResourceException("An archived Event keeps its Participants.");
        registrationRepository.delete(registration);
    }

    @Override
    public List<RegistrationDto> registrationsOf(UUID personId) {
        return registrationRepository.listByPerson(personId).stream().map(EventServiceImpl::toDto).toList();
    }

    private static RegistrationDto toDto(EventRegistrationEntity registration) {
        return new RegistrationDto(registration.getEvent().getEventId(), registration.getMode(), registration.status(), registration.getSignedUpAt());
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
