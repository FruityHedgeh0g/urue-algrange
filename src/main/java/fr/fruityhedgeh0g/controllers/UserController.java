package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicUserService;
import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@Path("/api/users")
public class UserController {
    @Inject
    JsonWebToken token;

    @Inject
    PublicUserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<UserDto> getAllUsers(){
        return userService.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/me")
    @Authenticated
    public @JsonView(Views.Detailed.class) UserDto getCurrentUser(){
        UUID userId = UUID.fromString(token.getSubject());
        return userService.getById(userId);
    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public @JsonView(Views.CreationResponse.class) UserDto addUser(@JsonView(Views.Creation.class) UserDto userDto){
//        return userService.createUser(userDto).get();
//    }
//
//    @PATCH
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public @JsonView(Views.UpdateResponse.class) UserDto updateUser(@JsonView(Views.Update.class) UserDto userDto){
//        return userService.updateUser(userDto).get();
//    }
//
//    @GET
//    @Path("/{userId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public @JsonView(Views.Detailed.class) UserDto getUserById(@PathParam("userId") UUID userId){
//        return userService.getUserById(userId).get();
//    }

}
