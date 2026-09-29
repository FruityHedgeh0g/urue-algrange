package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.dtos.eventDtos.EventStatusChangeDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicEventService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

/** Reading is open to anonymous Visiteurs (see quarkus.http.auth.permission.public-events); there is no delete. */
@Path("/api/events")
public class EventController {

    @Inject
    PublicEventService eventService;

    @Inject
    SecurityIdentity identity;

    private boolean seesPlanification() {
        return identity.hasRole("bureau");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<EventDto> getAllEvents(){
        return eventService.listAll(seesPlanification());
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
