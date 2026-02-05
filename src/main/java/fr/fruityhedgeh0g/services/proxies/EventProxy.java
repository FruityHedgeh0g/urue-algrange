package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Identifier("eventProxy")
@AllArgsConstructor
@ApplicationScoped
public class EventProxy implements EventService {
    @Inject
    EventService eventService;

    @Override
    public Try<List<EventDto>> getAllEvents() {
        return null;
    }

    @Override
    public Try<EventDto> getEventById(UUID eventId) {
        return null;
    }

    @Override
    public Try<EventDto> createEvent(EventDto eventDto) {
        return null;
    }

    @Override
    public Try<EventDto> updateEvent(EventDto eventDto) {
        return null;
    }

    @Override
    public void deleteEvent(UUID eventId) {

    }
}
