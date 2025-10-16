package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.model.dtos.configurations.FeatureDto;
import fr.fruityhedgeh0g.services.FeatureService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AllArgsConstructor;

import java.util.List;

@Path("/api/features")
public class FeatureController {

    @Inject
    FeatureService featureService;

    @GET
    @Path("/{name}")
    public FeatureDto getFeatureByName(@PathParam("name") String name) {
        return featureService.getFeatureByName(name)
                .getOrElseThrow(e -> new RuntimeException(e));
    }

    @Path("/all")
    @GET
    public List<FeatureDto> getAllFeatures(){
        return featureService.getAllFeatures()
                .getOrElseThrow(e -> new RuntimeException(e));
    }
}
