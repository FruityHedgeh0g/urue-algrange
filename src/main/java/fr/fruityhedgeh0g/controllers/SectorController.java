package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicSectorService;
import io.smallrye.common.annotation.Identifier;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/api/sectors")
public class SectorController {

    @Inject
    PublicSectorService sectorService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public @JsonView(Views.Basic.class) List<SectorDto> getAllSectors(){
        return sectorService.listAll();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{sectorId}")
    public @JsonView(Views.Detailed.class) Optional<SectorDto> getById(
            @PathParam("sectorId") UUID sectorId
    ){
        return sectorService.getById(sectorId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public @JsonView(Views.CreationResponse.class) SectorDto create(
            @JsonView(Views.Creation.class) SectorDto sectorDto
    ){
        return sectorService.create(sectorDto);
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public @JsonView(Views.UpdateResponse.class) SectorDto update(
            @JsonView(Views.Update.class) SectorDto sectorDto
    ){
        return sectorService.update(sectorDto);
    }

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{sectorId}")
    public void delete(
            @PathParam("sectorId") UUID sectorId
    ){
        sectorService.delete(sectorId);
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{sectorId}/group/{groupId}")
    public void assignGroup(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId){
        sectorService.assignGroup(sectorId,groupId);
    }

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{sectorId}/group/{groupId}")
    public void unassignGroup(@PathParam("sectorId") UUID sectorId, @PathParam("groupId") UUID groupId){
        sectorService.unassignGroup(sectorId,groupId);
    }
}
