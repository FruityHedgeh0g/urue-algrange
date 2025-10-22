package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.dtos.EventDto;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.repositories.RoleRepository;
import fr.fruityhedgeh0g.utilities.mappers.EventMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
        Log.info("Creating event: " + eventDto.getEventId());
        return null;
    }

    public Try<EventDto> updateEvent(@NotNull EventDto eventDto){
        Log.info("Updating event: " + eventDto.getEventId());
        return null;
    }

    public void deleteEvent(@NotNull UUID eventId){
        Log.info("Deleting event with id: " + eventId);
        Try.of(() -> eventRepository.deleteById(eventId))
                .onFailure(e -> Log.error("Error deleting event with id: " + eventId, e));
    }
}
