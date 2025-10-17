package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.model.dtos.UserDto;
import fr.fruityhedgeh0g.services.UserService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.User;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
//@Authenticated
public class UserController {

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

}
