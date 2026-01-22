package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.SectorDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.SectorService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@Path("/api/sectors")
public class SectorController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    SectorService sectorService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<SectorDto> getAllSectors(){
        return sectorService.getAllSectors().get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @JsonView(Views.CreationResponse.class) SectorDto addSector(@JsonView(Views.Creation.class) SectorDto sectorDto){
        return sectorService.createSector(sectorDto).get();
    }

    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}/assign/{groupId}")
    public SectorDto addGroupToSector(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId) {
        return sectorService.assignGroupToSector(sectorId,groupId).get();
    }

    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}/unassign/{groupId}")
    public SectorDto removeGroupFromSector(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId) {
        return sectorService.unassignGroupToSector(sectorId,groupId).get();
    }

    @PATCH
    @Consumes({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) SectorDto updateSector(SectorDto sectorDto) {
        return sectorService.updateSector(sectorDto).get();
    }

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{sectorId}")
    public void deleteSector(@PathParam("sectorId") UUID sectorId) {
        sectorService.deleteSector(sectorId);
    }
}
