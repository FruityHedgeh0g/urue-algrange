package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.dtos.EventDto;
import fr.fruityhedgeh0g.model.entities.EventEntity;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class EventService {

    @Inject
    EventRepository eventRepository;

    @Inject
    EventMapper eventMapper;

    public Try<List<EventDto>> getAllEvents(){
        Log.info("Getting all events");
        return Try.of(() -> eventRepository
                .findAll()
                .stream()
                .map(eventMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all events", e));
    }

    public Try<EventDto> getEventById(@NotNull UUID eventId){
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

    public Try<EventDto> createEvent(@NotNull EventDto eventDto){
        return Try.of(() -> {
            Log.debug("Searching for already existing event with name: " + eventDto.getName());
            if (eventRepository.existsByName(eventDto.getName())) {
                throw new DuplicateDataException();
            }

            Log.debug("Creating user: " + eventDto.getName());
            EventEntity eventEntity = eventMapper.toEntity(eventDto);
            eventRepository.persist(eventEntity);

            Log.debug("Event created, retrieving up-to-date event infos: " + eventEntity.getEventId());
            return eventMapper.toDto(
                    eventRepository
                            .findByIdOptional(eventEntity.getEventId())
                            .orElseThrow(NoSuchElementException::new)
            );
        }).onFailure(e -> {
            if (e instanceof DuplicateDataException) {
                Log.warn("Event already exists: " + eventDto.getName());
            }else {
                Log.error("Error creating event with name: " + eventDto.getName(), e);
            }
        });
    }

    //TODO : Développer l'update
    public Try<EventDto> updateEvent(@NotNull EventDto eventDto){
        Log.info("Updating event: " + eventDto.getEventId());
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    public void deleteEvent(@NotNull UUID eventId){
        Log.info("Deleting event with id: " + eventId);
        Try.of(() -> eventRepository.deleteById(eventId))
                .onFailure(e -> Log.error("Error deleting event with id: " + eventId, e));
    }
}
