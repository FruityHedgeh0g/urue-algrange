package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.EventService;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicEventService;
import io.smallrye.common.annotation.Identifier;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/events")
public class EventController {

    @Inject
    PublicEventService eventService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<EventDto> getAllEvents(){
        return eventService.listAll();
    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public @JsonView(Views.CreationResponse.class) EventDto addEvent(@JsonView(Views.Creation.class) EventDto eventDto){
//        return eventService.createEvent(eventDto).get();
//    }
}
