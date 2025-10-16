package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.model.dtos.configurations.ConfigurationDto;
import fr.fruityhedgeh0g.services.ConfigurationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

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
    @Path("/all")
    public List<ConfigurationDto> getAllConfigurations(){
        return configurationService.getAllConfigurations()
                .getOrElseThrow(e -> new RuntimeException(e));
    }
}
