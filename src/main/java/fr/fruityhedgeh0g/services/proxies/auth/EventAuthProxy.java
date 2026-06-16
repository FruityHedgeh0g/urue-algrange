package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Alternative
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
    public Try<List<EventDto>> listAll() {
        return null;
    }

    @Override
    public Try<EventDto> getById(UUID eventId) {
        return null;
    }

    @Override
    public Try<EventDto> create(EventDto eventDto) {
        return null;
    }

    @Override
    public Try<EventDto> update(EventDto eventDto) {
        return null;
    }

    @Override
    public Try<EventDto> delete(UUID eventId) {
        return null;
    }
}
