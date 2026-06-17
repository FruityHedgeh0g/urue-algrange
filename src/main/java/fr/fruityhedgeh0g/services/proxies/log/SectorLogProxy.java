package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import fr.fruityhedgeh0g.services.interfaces.internal.InternalGroupService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Decorator
@Priority(200)
public class SectorLogProxy implements SectorService {

    @Inject
    @Delegate
    SectorService sectorService;

    @Override
    public List<SectorDto> listAll() {
        Log.debugf("Trying to retrieve all sectors.");
        return Try.of(sectorService::listAll)
                .onSuccess(sectors -> Log.debugf("%d sectors retrieved.",sectors.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving sectors."))
                .get();
    }

    @Override
    public Optional<SectorDto> getById(UUID sectorId) {
        Log.debugf("Trying to retrieve sector by id %s.",sectorId);
        return Try.of(() -> sectorService.getById(sectorId))
                .onSuccess(sector -> {
                    if (sector.isPresent())
                        Log.debugf("Sector retrieved.");
                    else Log.debugf("There is no sector with id %s.",sectorId);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving sector."))
                .get();
    }

    @Override
    public SectorDto create(SectorDto sectorDto) {
        Log.debugf("Trying to create a new sector : %s", sectorDto.toString());
        return Try.of(() -> sectorService.create(sectorDto))
                .onSuccess(sector -> Log.debugf("Sector created."))
                .onFailure(t -> {
                    switch(t){
                        case DuplicateResourceException ex -> Log.errorf(ex,"A sector already exists for the id provided [%s].", sectorDto.getSectorId());
                        default -> Log.errorf(t,"An error occurred while creating sector.");
                    }
                })
                .get();
    }

    @Override
    public SectorDto update(SectorDto sectorDto) {
        Log.debugf("Trying to update an existing sector : %s", sectorDto.toString());
        return Try.of(() -> sectorService.update(sectorDto))
                .onSuccess(sector -> Log.debugf("Sector updated."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"There is no sector with id %s.", sectorDto.getSectorId());
                        case DuplicateResourceException ex -> Log.errorf(ex, "A sector already exists with this name [%s].", sectorDto.getName());
                        default -> Log.errorf(t,"An error occurred while updating sector.");
                    }
                })
                .get();
    }

    @Override
    public void delete(UUID sectorId) {
        Log.debugf("Trying to delete sector by id %s.",sectorId);
        Try.run(() -> sectorService.delete(sectorId))
                .onSuccess(v -> Log.debugf("Sector deleted."))
                .onFailure(t -> Log.errorf(t,"An error occurred during sector deletion."))
                .get();
    }

    @Override
    public void assignGroup(UUID sectorId, UUID groupId) {

    }

    @Override
    public void unassignGroup(UUID sectorId, UUID groupId) {

    }
}
