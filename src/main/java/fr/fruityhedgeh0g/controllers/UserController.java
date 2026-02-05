package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.UserServiceImpl;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@Path("/api/users")
//@Authenticated
public class UserController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    UserServiceImpl userServiceImpl;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<UserDto> getAllUsers(){
        return userServiceImpl.getAllUsers().get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @JsonView(Views.CreationResponse.class) UserDto addUser(@JsonView(Views.Creation.class) UserDto userDto){
        return userServiceImpl.createUser(userDto).get();
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @JsonView(Views.UpdateResponse.class) UserDto updateUser(@JsonView(Views.Update.class) UserDto userDto){
        return userServiceImpl.updateUser(userDto).get();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Detailed.class) UserDto getUserById(@PathParam("userId") UUID userId){
        return userServiceImpl.getUserById(userId).get();
    }

}
