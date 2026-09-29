package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicSectorService {
    List<SectorDto> listAll();
    SectorDto getById(@NotNull UUID sectorId);
    SectorDto create(@NotNull @Valid SectorDto sectorDto);
    SectorDto update(@NotNull @Valid SectorDto sectorDto);
    void delete(@NotNull UUID sectorId);
    void assignGroup(@NotNull UUID sectorId, @NotNull UUID groupId);
    void unassignGroup(@NotNull UUID sectorId, @NotNull UUID groupId);
    SectorDto getByUserId(@NotNull UUID userId);

//    Try<List<SectorDto>> getAllSectors();
//    Try<SectorDto> getSectorById(@NotNull UUID sectorId);
//    Try<SectorDto> createSector(@NotNull @Valid SectorDto sectorDto);
//    Try<SectorDto> updateSector(@NotNull @Valid SectorDto sectorDto);
//    Try<Void> deleteSector(@NotNull UUID sectorId);
//    Try<SectorDto> assignGroupToSector(@NotNull UUID sectorId, @NotNull UUID groupId);
//    Try<SectorDto> unassignGroupFromSector(@NotNull UUID sectorId, @NotNull UUID groupId);

}

