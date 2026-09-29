package fr.fruityhedgeh0g.services.decorators.logs;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.enums.EventStatusEnum;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@Priority(200)
@Decorator
public class EventLogDecorator implements EventService{

    @Inject
    @Delegate
    EventService eventService;

    @Override
    public List<EventDto> listAll(boolean seesPlanification) {
        Log.debugf("Retrieving all events...");
        return Try.of(() -> eventService.listAll(seesPlanification))
                .onSuccess(events -> Log.debugf("%d events retrieved.",events.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving events."))
                .get();
    }

    @Override
    public EventDto getById(UUID eventId, boolean seesPlanification) {
        Log.debugf("Retrieving event by id %s...",eventId);
        return Try.of(() -> eventService.getById(eventId, seesPlanification))
                .onSuccess(event -> Log.debugf("Event retrieved: "+event.toString()))
                .onFailure(t -> {
                    switch (t) {
                        case UnknownResourceException ex -> Log.warnf("Event with id %s not found.", eventId);
                        default -> Log.errorf(t, "An error occurred while retrieving event.");
                    }
                })
                .get();
    }

    @Override
    public EventDto create(EventDto eventDto) {
        Log.debugf("Creating event %s...", eventDto.getName());
        return Try.of(() -> eventService.create(eventDto))
                .onSuccess(event -> Log.infof("Event %s created.", event.getEventId()))
                .onFailure(t -> {
                    switch (t) {
                        case InvalidResourceException ex -> Log.warnf("Event refused: %s", ex.getMessage());
                        case UnknownResourceException ex -> Log.warnf("Event refused, unknown Secteur: %s", ex.getMessage());
                        default -> Log.errorf(t, "An error occurred while creating event.");
                    }
                })
                .get();
    }

    @Override
    public EventDto update(EventDto eventDto) {
        Log.debugf("Updating event %s...", eventDto.getEventId());
        return Try.of(() -> eventService.update(eventDto))
                .onSuccess(event -> Log.debugf("Event updated."))
                .onFailure(t -> {
                    switch (t) {
                        case InvalidResourceException ex -> Log.warnf("Event update refused: %s", ex.getMessage());
                        case UnknownResourceException ex -> Log.warnf("Event %s not found.", eventDto.getEventId());
                        default -> Log.errorf(t, "An error occurred while updating event.");
                    }
                })
                .get();
    }

    @Override
    public EventDto changeStatus(UUID eventId, EventStatusEnum status) {
        Log.debugf("Moving event %s to %s...", eventId, status.id());
        return Try.of(() -> eventService.changeStatus(eventId, status))
                .onSuccess(event -> Log.infof("Event %s is now %s.", eventId, status.id()))
                .onFailure(t -> {
                    switch (t) {
                        case InvalidResourceException ex -> Log.warnf("Status change refused: %s", ex.getMessage());
                        case UnknownResourceException ex -> Log.warnf("Event %s not found.", eventId);
                        default -> Log.errorf(t, "An error occurred while changing the event status.");
                    }
                })
                .get();
    }
}
