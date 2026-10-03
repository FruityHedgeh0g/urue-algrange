package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicSectorService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Only the Super admin opens, renames, closes and reopens a Secteur; the Bureau keeps its description
 * and places Groupes in it. A Secteur is closed (fermé), never deleted: see ADR 0003.
 */
@Path("/sectors")
public class SectorController {
    @Inject
    PublicSectorService sectorService;

    @Inject
    SecurityIdentity identity;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public @JsonView(Views.Detailed.class) List<SectorDto> getAllSectors(){
        return sectorService.listAll();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}")
    public @JsonView(Views.Detailed.class) SectorDto getById(
            @PathParam("sectorId") UUID sectorId
    ){
        return sectorService.getById(sectorId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @RolesAllowed("super_admin")
    public @JsonView(Views.CreationResponse.class) SectorDto create(
            @JsonView(Views.Creation.class) SectorDto sectorDto
    ){
        return sectorService.create(sectorDto);
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    @RolesAllowed("bureau")
    public @JsonView(Views.UpdateResponse.class) SectorDto update(
            @JsonView(Views.Update.class) SectorDto sectorDto
    ){
        return sectorService.update(sectorDto, identity.hasRole("super_admin"));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}/close")
    @RolesAllowed("super_admin")
    public @JsonView(Views.Basic.class) SectorDto close(@PathParam("sectorId") UUID sectorId){
        return sectorService.close(sectorId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}/reopen")
    @RolesAllowed("super_admin")
    public @JsonView(Views.Basic.class) SectorDto reopen(@PathParam("sectorId") UUID sectorId){
        return sectorService.reopen(sectorId);
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{sectorId}/group/{groupId}")
    @RolesAllowed("bureau")
    public void assignGroup(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId){
        sectorService.assignGroup(sectorId,groupId);
    }

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{sectorId}/group/{groupId}")
    @RolesAllowed("bureau")
    public void unassignGroup(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId){
        sectorService.unassignGroup(sectorId,groupId);
    }

}
