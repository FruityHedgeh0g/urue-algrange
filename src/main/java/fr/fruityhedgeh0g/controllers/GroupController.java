package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.GroupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.GroupService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/groups")
public class GroupController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    GroupService groupService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<GroupDto> getAllGroups(){
        return groupService.getAllGroups().get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @JsonView(Views.CreationResponse.class) GroupDto addGroup(@JsonView(Views.Creation.class) GroupDto groupDto){
        return groupService.createGroup(groupDto).get();
    }
}


