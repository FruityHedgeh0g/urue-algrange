package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.util.ViewMatcher;
import fr.fruityhedgeh0g.model.dtos.UserDto;
import fr.fruityhedgeh0g.model.dtos.Views;
import fr.fruityhedgeh0g.services.UserService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.User;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

@Path("/api/users")
//@Authenticated
public class UserController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/all")
    public @JsonView(Views.Basic.class) List<UserDto> getAllUsers(){
        return userService.getAllUsers().get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public @JsonView(Views.System.class) UserDto addUser( @JsonView(Views.Creation.class) UserDto userDto){
        return userService.createUser(userDto).get();
    }

}
