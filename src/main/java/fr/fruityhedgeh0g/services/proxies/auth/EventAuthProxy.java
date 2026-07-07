package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@Priority(100)
@Authenticated
@Decorator
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class EventAuthProxy implements EventService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    EventService eventService;


    @Override
    public List<EventDto> listAll() {
        return eventService.listAll();
    }

    @Override
    public EventDto getById(UUID eventId) {
        return eventService.getById(eventId);
    }

    @Override
    public EventDto create(EventDto eventDto) {
        return eventService.create(eventDto);
    }

    @Override
    public EventDto update(EventDto eventDto) {
        return eventService.update(eventDto);
    }

    @Override
    public void delete(UUID eventId) {
        eventService.delete(eventId);
    }
}
