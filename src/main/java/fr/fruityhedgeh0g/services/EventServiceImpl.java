package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.entities.EventEntity;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class EventServiceImpl implements EventService {

    @Inject
    EventRepository eventRepository;

    @Inject
    EventMapper eventMapper;

    @Transactional
    public Try<List<EventDto>> getAllEvents(){
        Log.info("Getting all events");
        return Try.of(() -> eventRepository
                .findAll()
                .stream()
                .map(eventMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all events", e));
    }

    @Transactional
    public Try<EventDto> getEventById( UUID eventId){
        Log.info("Getting event with id: " + eventId);
        return Try.of(() -> eventRepository.findByIdOptional(eventId)
                .orElseThrow(NoSuchElementException::new))
                .map(eventMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Event not found: " + eventId);
                    }else {
                        Log.error("Error getting event with id: " + eventId, e);
                    }
                });
    }

    @Transactional
    public Try<EventDto> createEvent( EventDto eventDto){
        return Try.of(() -> {
            Log.debug("Searching for already existing event with name: " + eventDto.getName());
            if (eventRepository.existsByName(eventDto.getName())) {
                throw new DuplicateResourceException("Event already exists: " + eventDto.getName() + "");
            }

            Log.debug("Creating user: " + eventDto.getName());
            EventEntity eventEntity = eventMapper.toEntity(eventDto);
            eventRepository.persist(eventEntity);

            Log.debug("Event created, retrieving up-to-date event infos: " + eventEntity.getEventId());
            return eventMapper.toDto(
                    eventRepository
                            .findByIdOptional(eventEntity.getEventId())
                            .orElseThrow(() -> new UnknownResourceException("Event not found:" + eventEntity.getEventId()))
            );
        }).onFailure(e -> {
            if (e instanceof DuplicateResourceException) {
                Log.warn("Event already exists: " + eventDto.getName());
            }else {
                Log.error("Error creating event with name: " + eventDto.getName(), e);
            }
        });
    }

    //TODO : Développer l'update
    @Transactional
    public Try<EventDto> updateEvent( EventDto eventDto){
        Log.info("Updating event: " + eventDto.getEventId());
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    @Transactional
    public void deleteEvent( UUID eventId){
        Log.info("Deleting event with id: " + eventId);
        Try.of(() -> eventRepository.deleteById(eventId))
                .onFailure(e -> Log.error("Error deleting event with id: " + eventId, e));
    }
}
