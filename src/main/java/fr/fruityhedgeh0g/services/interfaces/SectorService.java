package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SectorService {
    List<SectorDto> listAll();
    Optional<SectorDto> getById(@NotNull UUID sectorId);
    SectorDto create(@NotNull @Valid SectorDto sectorDto);
    SectorDto update(@NotNull @Valid SectorDto sectorDto);
    void delete(@NotNull UUID sectorId);
    void assignGroup(@NotNull UUID sectorId, @NotNull UUID groupId);
    void unassignGroup(@NotNull UUID sectorId, @NotNull UUID groupId);

//    Try<List<SectorDto>> getAllSectors();
//    Try<SectorDto> getSectorById(@NotNull UUID sectorId);
//    Try<SectorDto> createSector(@NotNull @Valid SectorDto sectorDto);
//    Try<SectorDto> updateSector(@NotNull @Valid SectorDto sectorDto);
//    Try<Void> deleteSector(@NotNull UUID sectorId);
//    Try<SectorDto> assignGroupToSector(@NotNull UUID sectorId, @NotNull UUID groupId);
//    Try<SectorDto> unassignGroupFromSector(@NotNull UUID sectorId, @NotNull UUID groupId);

}

