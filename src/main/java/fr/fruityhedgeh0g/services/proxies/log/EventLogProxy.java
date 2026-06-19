package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(200)
@Decorator
public class EventLogProxy implements EventService{

    @Inject
    @Delegate
    EventService eventService;

    @Override
    public List<EventDto> listAll() {
        Log.debugf("Retrieving all events...");
        return Try.of(eventService::listAll)
                .onSuccess(events -> Log.debugf("%d events retrieved.",events.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving events."))
                .get();
    }

    @Override
    public Optional<EventDto> getById(UUID eventId) {
        Log.debugf("Retrieving event by id %s...",eventId);
        return Try.of(() -> eventService.getById(eventId))
                .onSuccess(event -> {
                    if (event.isPresent())
                        Log.debugf("Event retrieved: "+event.toString());
                    else Log.debugf("Event with id %s not found.",eventId);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving event."))
                .get();
    }

    @Override
    public EventDto create(EventDto eventDto) {
        return null;
    }

    @Override
    public EventDto update(EventDto eventDto) {
        return null;
    }

    @Override
    public void delete(UUID eventId) {

    }
}
