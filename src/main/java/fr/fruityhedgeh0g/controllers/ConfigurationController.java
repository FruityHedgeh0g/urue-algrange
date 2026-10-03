package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationValueDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.ConfigurationService;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicConfigurationService;
import io.smallrye.common.annotation.Identifier;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * Site-wide settings (title, tagline, contact, logo): read by everyone, as the public site shows them;
 * changed by the Super admin only, who manages what is common to every Secteur (ADR 0004).
 */
@Path("/configurations")
public class ConfigurationController {

    @Inject PublicConfigurationService configurationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<ConfigurationDto> getAllConfigurations(){
        return configurationService.listAll();
    }

    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("super_admin")
    public @JsonView(Views.Basic.class) ConfigurationDto update(@PathParam("name") String name, @NotNull ConfigurationValueDto change){
        if (change.value() == null) throw new BadRequestException("A setting has a value.");
        return configurationService.update(ConfigurationDto.builder().name(name).value(change.value()).build());
    }

//    @GET
//    @Path("/{name}")
//    public ConfigurationDto getConfigurationByName(@PathParam("name") String name) {
//        return configurationService.getConfigurationByName(name)
//                .getOrElseThrow(e -> new RuntimeException(e));
//    }

}
