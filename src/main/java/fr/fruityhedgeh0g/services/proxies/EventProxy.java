package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Authenticated
@Identifier("serviceProxy")
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
