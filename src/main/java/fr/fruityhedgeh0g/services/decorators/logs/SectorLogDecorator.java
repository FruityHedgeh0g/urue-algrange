package fr.fruityhedgeh0g.services.decorators.logs;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@Decorator
@Priority(200)
public class SectorLogDecorator implements SectorService {

    @Inject
    @Delegate
    SectorService sectorService;

    @Override
    public List<SectorDto> listAll() {
        Log.debugf("Retrieving all sectors...");
        return Try.of(sectorService::listAll)
                .onSuccess(sectors -> Log.debugf("%d sectors retrieved.",sectors.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving sectors."))
                .get();
    }

    @Override
    public SectorDto getById(UUID sectorId) {
        Log.debugf("Retrieving sector by id %s...",sectorId);
        return Try.of(() -> sectorService.getById(sectorId))
                .onSuccess(sector -> {
                        Log.debugf("Sector retrieved: "+sector.toString());
                })
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"Sector with id %s not found.", sectorId);
                        default -> Log.errorf(t,"An error occurred while retrieving sector.");
                    }
                })
                .get();
    }

    @Override
    public SectorDto create(SectorDto sectorDto) {
        Log.debugf("Creating new sector: %s", sectorDto.toString());
        return Try.of(() -> sectorService.create(sectorDto))
                .onSuccess(sector -> Log.debugf("Sector created."))
                .onFailure(t -> {
                    switch(t){
                        case DuplicateResourceException ex -> Log.errorf(ex,"Sector %s already existing.", sectorDto.getSectorId());
                        default -> Log.errorf(t,"An error occurred while creating sector.");
                    }
                })
                .get();
    }

    @Override
    public SectorDto update(SectorDto sectorDto) {
        Log.debugf("Updating an existing sector: %s", sectorDto.toString());
        return Try.of(() -> sectorService.update(sectorDto))
                .onSuccess(sector -> Log.debugf("Sector updated."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"Sector %s not found.", sectorDto.getSectorId());
                        case DuplicateResourceException ex -> Log.errorf(ex, "A sector already exists with this name [%s].", sectorDto.getName());
                        default -> Log.errorf(t,"An error occurred while updating sector.");
                    }
                })
                .get();
    }

    @Override
    public void delete(UUID sectorId) {
        Log.debugf("Deleting sector by id %s...",sectorId);
        Try.run(() -> sectorService.delete(sectorId))
                .onSuccess(v -> Log.debugf("Sector deleted."))
                .onFailure(t -> Log.errorf(t,"An error occurred during sector deletion."))
                .get();
    }

    @Override
    public void assignGroup(UUID sectorId, UUID groupId) {
        //Todo: to detail
        Log.debugf("Assigning group %s to sector %s...",groupId,sectorId);
        Try.run(() -> sectorService.assignGroup(sectorId,groupId))
                .onSuccess(v -> Log.debugf("Group assigned to sector."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"Sector %s or group %s not found.", sectorId, groupId);
                        case DuplicateResourceException ex -> Log.errorf(ex, "Group %s is already assigned to another sector.", groupId);
                        default -> Log.errorf(t,"An error occurred while assigning group to sector.");
                    }
                })
                .get();
    }

    @Override
    public void unassignGroup(UUID sectorId, UUID groupId) {
        //Todo: to detail
        Log.debugf("Unassigning group %s from sector %s...",groupId,sectorId);
        Try.run(() -> sectorService.unassignGroup(sectorId,groupId))
                .onSuccess(v -> Log.debugf("Group unassigned from sector."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"Sector %s or group %s not found.", sectorId, groupId);
                        case InvalidResourceException ex -> Log.errorf(ex, "Group %s is assigned to another sector.", groupId);
                        default -> Log.errorf(t,"An error occurred while unassigning group from sector.");
                    }
                })
                .get();
    }
}
