package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("proxy")
@Authenticated
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class EventProxy implements EventService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
    EventService eventService;

    @Override
    public Try<List<EventDto>> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Override
    public Try<List<EventDto>> getAllEventsFiltered(String filter) {
        return eventService.getAllEventsFiltered(filter);
    }

    @Override
    public Try<EventDto> getEventById(UUID eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public Try<EventDto> createEvent(EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @Override
    public Try<EventDto> updateEvent(EventDto eventDto) {
        return eventService.updateEvent(eventDto);
    }

    @Override
    public Try<Void> deleteEvent(UUID eventId) {

        return eventService.deleteEvent(eventId);
    }

    @Override
    public Try<EventDto> addParticipant(UUID eventId, UUID userId) {
        return eventService.addParticipant(eventId, userId);
    }

    @Override
    public Try<EventDto> removeParticipant(UUID eventId, UUID userId) {
        return eventService.removeParticipant(eventId, userId);
    }

    @Override
    public Try<EventDto> addOrganizer(UUID eventId, UUID userId) {
        return eventService.addOrganizer(eventId, userId);
    }

    @Override
    public Try<EventDto> removeOrganizer(UUID eventId, UUID userId) {
        return eventService.removeOrganizer(eventId, userId);
    }
}
