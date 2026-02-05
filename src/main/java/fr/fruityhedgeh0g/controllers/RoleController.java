package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import fr.fruityhedgeh0g.services.proxies.RoleProxy;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@Path("/api/roles")
public class RoleController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier( "roleProxy")
    RoleService roleService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<RoleDto> getAllRoles(){
        return roleService.getAllRoles().get();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{roleId}")
    public @JsonView(Views.Detailed.class) RoleDto getRoleById(@PathParam("roleId") UUID roleId){
        return roleService.getRoleById(roleId).get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @JsonView(Views.CreationResponse.class) RoleDto addRole(@JsonView(Views.Creation.class) RoleDto roleDto){
        return roleService.createRole(roleDto).get();
    }


}
