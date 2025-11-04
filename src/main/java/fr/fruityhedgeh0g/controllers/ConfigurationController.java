package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.configurations.ConfigurationDto;
import fr.fruityhedgeh0g.services.ConfigurationService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/configurations")
public class ConfigurationController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

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
    @Path("/get/all")
    public @JsonView(ConfigurationDto.Basic.class) List<ConfigurationDto> getAllConfigurations(){
        return configurationService.getAllConfigurations().get();
    }
}
