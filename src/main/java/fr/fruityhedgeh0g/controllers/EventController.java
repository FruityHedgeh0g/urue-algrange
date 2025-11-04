package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.EventDto;
import fr.fruityhedgeh0g.services.EventService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/events")
public class EventController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject EventService eventService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/all")
    public @JsonView(EventDto.Basic.class) List<EventDto> getAllEvents(){
        return eventService.getAllEvents().get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public @JsonView(EventDto.Extended.class) EventDto addEvent( @JsonView(EventDto.Creation.class) EventDto eventDto){
        return eventService.createEvent(eventDto).get();
    }
}
