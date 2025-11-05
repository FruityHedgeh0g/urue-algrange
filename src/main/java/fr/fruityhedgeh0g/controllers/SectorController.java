package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.GroupDto;
import fr.fruityhedgeh0g.model.dtos.SectorDto;
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
    @Path("/get/all")
    public @JsonView(SectorDto.Extended.class) List<SectorDto> getAllSectors(){
        return sectorService.getAllSectors().get();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public @JsonView(SectorDto.Extended.class) SectorDto addSector(@JsonView(SectorDto.Creation.class) SectorDto sectorDto){
        return sectorService.createSector(sectorDto).get();
    }

    @PATCH
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}/assign/{groupId}")
    public @JsonView(SectorDto.Extended.class) SectorDto addGroupToSector(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId) {
        return sectorService.assignGroupToSector(sectorId,groupId).get();
    }

    @PATCH
    @Consumes({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public @JsonView(SectorDto.Basic.class) SectorDto updateSector(@JsonView(SectorDto.Basic.class) SectorDto sectorDto) {
        return sectorService.updateSector(sectorDto).get();
    }
}
