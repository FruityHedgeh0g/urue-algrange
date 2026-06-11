package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.ConfigurationService;
import io.smallrye.common.annotation.Identifier;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/configurations")
public class ConfigurationController {

    @Inject
    ConfigurationService configurationService;

    @GET
    @Path("/{name}")
    public ConfigurationDto getConfigurationByName(@PathParam("name") String name) {
        return configurationService.getConfigurationByName(name)
                .getOrElseThrow(e -> new RuntimeException(e));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<ConfigurationDto> getAllConfigurations(){
        return configurationService.getAllConfigurations().get();
    }
}
