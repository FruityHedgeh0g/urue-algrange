package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.dtos.eventDtos.EventStatusChangeDto;
import fr.fruityhedgeh0g.dtos.eventDtos.RegistrationDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicEventService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

/** Reading Events is open to anonymous Visiteurs (see quarkus.http.auth.permission.public-events); Events are never deleted. */
@Path("/api/events")
public class EventController {

    @Inject
    PublicEventService eventService;

    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    private UUID me() {
        return UUID.fromString(token.getSubject());
    }

    private boolean seesPlanification() {
        return identity.hasRole("bureau");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<EventDto> getAllEvents(){
        return eventService.listAll(seesPlanification());
    }

    /** The current person's sign-ups ("Mes événements"). */
    @GET
    @Path("/registrations")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("benevole")
    public List<RegistrationDto> getMyRegistrations(){
        return eventService.registrationsOf(me());
    }

    @PUT
    @Path("/{eventId}/registration")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("benevole")
    public RegistrationDto signUp(@PathParam("eventId") UUID eventId){
        return eventService.signUp(eventId, me());
    }

    @DELETE
    @Path("/{eventId}/registration")
    @RolesAllowed("benevole")
    public void withdraw(@PathParam("eventId") UUID eventId){
        eventService.withdraw(eventId, me());
    }

    @GET
    @Path("/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Detailed.class) EventDto getEvent(@PathParam("eventId") UUID eventId){
        return eventService.getById(eventId, seesPlanification());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("bureau")
    public @JsonView(Views.CreationResponse.class) EventDto addEvent(@JsonView(Views.Creation.class) EventDto eventDto){
        return eventService.create(eventDto);
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("bureau")
    public @JsonView(Views.Detailed.class) EventDto updateEvent(@JsonView(Views.Update.class) EventDto eventDto){
        return eventService.update(eventDto);
    }

    @PUT
    @Path("/{eventId}/status")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("bureau")
    public @JsonView(Views.Detailed.class) EventDto changeStatus(@PathParam("eventId") UUID eventId, @Valid @NotNull EventStatusChangeDto change){
        return eventService.changeStatus(eventId, change.status());
    }
}
