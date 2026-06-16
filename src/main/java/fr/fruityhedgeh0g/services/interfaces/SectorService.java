package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface SectorService {
    Try<List<SectorDto>> listAll();
    Try<SectorDto> getById(@NotNull UUID sectorId);
    Try<SectorDto> create(@NotNull @Valid SectorDto sectorDto);
    Try<SectorDto> update(@NotNull @Valid SectorDto sectorDto);
    Try<SectorDto> delete(@NotNull UUID sectorId);

//    Try<List<SectorDto>> getAllSectors();
//    Try<SectorDto> getSectorById(@NotNull UUID sectorId);
//    Try<SectorDto> createSector(@NotNull @Valid SectorDto sectorDto);
//    Try<SectorDto> updateSector(@NotNull @Valid SectorDto sectorDto);
//    Try<Void> deleteSector(@NotNull UUID sectorId);
//    Try<SectorDto> assignGroupToSector(@NotNull UUID sectorId, @NotNull UUID groupId);
//    Try<SectorDto> unassignGroupFromSector(@NotNull UUID sectorId, @NotNull UUID groupId);


}
