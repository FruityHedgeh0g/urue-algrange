package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicGroupService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/groups")
public class GroupController {

    @Inject
    PublicGroupService groupService;

    /** Open to anonymous Visiteurs (see quarkus.http.auth.permission.public-groups). */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<GroupDto> getAllGroups(){
        return groupService.listAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("bureau")
    public @JsonView(Views.CreationResponse.class) GroupDto addGroup(@JsonView(Views.Creation.class) GroupDto groupDto){
        return groupService.create(groupDto);
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("bureau")
    public @JsonView(Views.Basic.class) GroupDto updateGroup(@JsonView(Views.Update.class) GroupDto groupDto){
        return groupService.update(groupDto);
    }

    @PUT
    @Path("/{groupId}/chef/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("bureau")
    public @JsonView(Views.Basic.class) GroupDto setChef(@PathParam("groupId") UUID groupId, @PathParam("userId") UUID userId){
        return groupService.setChef(groupId, userId);
    }

    @DELETE
    @Path("/{groupId}/chef")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("bureau")
    public @JsonView(Views.Basic.class) GroupDto clearChef(@PathParam("groupId") UUID groupId){
        return groupService.clearChef(groupId);
    }
}
