package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.dtos.SectorDto;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.utilities.mappers.SectorMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class SectorService {
    @Inject
    SectorRepository sectorRepository;

    @Inject
    SectorMapper sectorMapper;

    public Try<List<SectorDto>> getAllSectors() {
        Log.info("Getting all sectors");
        return Try.of(() -> sectorRepository
                .findAll()
                .stream()
                .map(sectorMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all sectors", e));
    }

    public Try<SectorDto> getSectorById(@NotNull UUID sectorId) {
        Log.info("Getting sector with id: " + sectorId);
        return Try.of(() -> sectorRepository
                        .findByIdOptional(sectorId)
                        .orElseThrow(NoSuchElementException::new))
                .map(sectorMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Sector not found: " + sectorId);
                    }else {
                        Log.error("Error getting sector with id: " + sectorId, e);
                    }
                });
    }

    public Try<SectorDto> createSector(@NotNull SectorDto sectorDto) {
        Log.info("Creating sector: " + sectorDto.getSectorId());
        return null;
    }

    public Try<SectorDto> updateSector(@NotNull SectorDto sectorDto) {
        Log.info("Updating sector: " + sectorDto.getSectorId());
        return null;
    }

    public void deleteSector(@NotNull UUID sectorId) {
        Log.info("Deleting sector with id: " + sectorId);
        Try.run(() -> sectorRepository.deleteById(sectorId))
                .onFailure(e -> Log.error("Error deleting sector with id: " + sectorId, e));
    }
}
