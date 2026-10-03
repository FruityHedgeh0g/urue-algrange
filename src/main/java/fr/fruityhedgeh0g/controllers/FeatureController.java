package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.dtos.featureDtos.FeatureSwitchDto;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicFeatureService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * Fonctionnalités: read by everyone, as the public site follows them (donations, the gallery, sign-ups);
 * turned on and off by the Super admin only, who manages what is common to every Secteur (ADR 0004).
 */
@Path("/features")
@Produces(MediaType.APPLICATION_JSON)
public class FeatureController {

    @Inject
    PublicFeatureService featureService;

    @GET
    public @JsonView(Views.Basic.class) List<FeatureDto> getAll(){
        return featureService.listAll();
    }

    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("super_admin")
    public @JsonView(Views.Basic.class) FeatureDto setActive(@PathParam("name") String name, @NotNull FeatureSwitchDto change){
        if (change.isActive() == null) throw new BadRequestException("A feature is turned on or off.");
        return featureService.update(FeatureDto.builder().name(name).isActive(change.isActive()).build());
    }
}
