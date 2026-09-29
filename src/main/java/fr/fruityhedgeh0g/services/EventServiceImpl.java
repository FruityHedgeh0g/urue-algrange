package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.utilities.logging.Logged;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.entities.EventEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.enums.EventStatusEnum;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalSectorService;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
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
