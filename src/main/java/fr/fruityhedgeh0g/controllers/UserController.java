package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/api/users")
public class UserController {

    @Inject
    @Identifier("proxy")
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<UserDto> getAllUsers(){
        List<UserDto> users = userService.getAllUsers().get();
        Log.debugf("Retrieved %s users", users.toString());
        return users;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @JsonView(Views.CreationResponse.class) UserDto addUser(@JsonView(Views.Creation.class) UserDto userDto){
        return userService.createUser(userDto).get();
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @JsonView(Views.UpdateResponse.class) UserDto updateUser(@JsonView(Views.Update.class) UserDto userDto){
        return userService.updateUser(userDto).get();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Detailed.class) UserDto getUserById(@PathParam("userId") UUID userId){
        return userService.getUserById(userId).get();
    }

}
