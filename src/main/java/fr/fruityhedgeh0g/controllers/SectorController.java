package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.SectorServiceImpl;
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
    SectorServiceImpl sectorServiceImpl;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<SectorDto> getAllSectors(){
        return sectorServiceImpl.getAllSectors().get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @JsonView(Views.CreationResponse.class) SectorDto addSector(@JsonView(Views.Creation.class) SectorDto sectorDto){
        return sectorServiceImpl.createSector(sectorDto).get();
    }

    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}/assign/{groupId}")
    public @JsonView(Views.Detailed.class) SectorDto addGroupToSector(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId) {
        return sectorServiceImpl.assignGroupToSector(sectorId,groupId).get();
    }

    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}/unassign/{groupId}")
    public @JsonView(Views.Detailed.class) SectorDto removeGroupFromSector(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId) {
        return sectorServiceImpl.unassignGroupFromSector(sectorId,groupId).get();
    }

    @PATCH
    @Consumes({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.UpdateResponse.class) SectorDto updateSector(@JsonView(Views.Update.class) SectorDto sectorDto) {
        return sectorServiceImpl.updateSector(sectorDto).get();
    }

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{sectorId}")
    public void deleteSector(@PathParam("sectorId") UUID sectorId) {
        sectorServiceImpl.deleteSector(sectorId);
    }
}
