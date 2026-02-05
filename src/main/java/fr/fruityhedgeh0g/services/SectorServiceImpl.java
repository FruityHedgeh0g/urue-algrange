package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidInputException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import fr.fruityhedgeh0g.utilities.mappers.SectorMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class SectorServiceImpl implements SectorService {
    @Inject
    SectorRepository sectorRepository;

    @Inject()
    GroupService groupService;

    @Inject
    SectorMapper sectorMapper;

    @Transactional
    public Try<List<SectorDto>> getAllSectors() {
        Log.info("Getting all sectors");
        return Try.of(() -> sectorRepository
                .findAll()
                .stream()
                .map(sectorMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all sectors", e));
    }

    @Transactional
    public Try<SectorDto> getSectorById( UUID sectorId) {
        Log.info("Getting sector with id: " + sectorId);
        return Try.of(() -> sectorRepository
                        .findByIdOptional(sectorId)
                        .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorId)))
                .map(sectorMapper::toDto)
                .onFailure(ex -> {
                    if (Objects.requireNonNull(ex) instanceof UnknownResourceException e) {
                        Log.warn(e.getMessage());
                    } else {
                        Log.error("Error getting sector with id: " + sectorId, ex);
                    }
                });
    }

    @Transactional
    public Try<SectorDto> createSector( SectorDto sectorDto) {
        return Try.of(() -> {
            Log.debug("Searching for already existing sector with name: " + sectorDto.getName());
            if (sectorRepository.existsByName(sectorDto.getName())) {throw new DuplicateResourceException("A sector with the same name already exists");}

            Log.debug("Checking if all groups exist and doesn't belong to another sector");
            SectorEntity sectorEntity = sectorMapper.toEntity(sectorDto);

            Log.debug("Creating sector: " + sectorDto.getName());

            sectorRepository.persist(sectorEntity);

            Log.debug("Sector created, retrieving up-to-date sector infos: " + sectorEntity.getSectorId());
            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            switch (ex) {
                case UnknownResourceException e -> Log.warn(e.getMessage());
                case InvalidInputException e -> Log.warn(e.getMessage());
                default -> Log.error("Error creating sector with name: " + sectorDto.getName(), ex);
            }
        });
    }

    @Transactional
    public Try<SectorDto> assignGroupToSector( UUID sectorId,  UUID groupId) {
        return Try.of(()-> {
            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
                    .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));
            GroupEntity groupEntity = groupService.getInternalEntityById(groupId)
                    .getOrElseThrow(e -> {throw new UnknownResourceException("Group not found: "+groupId);});

            if (groupEntity.getSector() != null) throw new InvalidInputException("Group already belongs to a sector");

            //sectorEntity.addGroup(groupEntity);
            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            switch (ex) {
                case UnknownResourceException e -> Log.warn(e.getMessage());
                default -> Log.error("Error assigning group to sector with id: " + sectorId, ex);
            }
        });
    }

    @Transactional
    public Try<SectorDto> unassignGroupFromSector( UUID sectorId,  UUID groupId) {
        return Try.of(() -> sectorRepository.findByIdOptional(sectorId)
                .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorId)))
                .peek(sector -> {
                    GroupEntity groupEntity = groupService.getInternalEntityById(groupId)
                            .getOrElseThrow(e -> new UnknownResourceException("Group not found:" +groupId ));
                    sector.removeGroup(groupEntity);
                }).map(sectorMapper::toDto)
                .onFailure(ex -> {
                    switch (ex) {
                        case UnknownResourceException e -> Log.warn(e.getMessage());
                        default -> Log.error("Error unassigning group to sector with id: " + sectorId, ex);
                    }
                });
    }

    @Transactional
    public Try<SectorDto> updateSector( SectorDto sectorDto) {
        return Try.of(() -> {
            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorDto.getSectorId())
                    .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorDto.getSectorId()));

            sectorMapper.partialDtoToEntity(sectorEntity,sectorDto);

            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            if (Objects.requireNonNull(ex) instanceof UnknownResourceException e) {
                Log.warn(e.getMessage());
            } else {
                Log.error("Error updating sector with id: " + sectorDto.getSectorId(), ex);
            }
        });
    }

    @Transactional
    public void deleteSector( UUID sectorId) {
        Try.of(() -> sectorRepository.findByIdOptional(sectorId)
                        .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorId)))
                .peek(sector -> sectorRepository.delete(sector))
                .onFailure(ex -> {
                    if (Objects.requireNonNull(ex) instanceof UnknownResourceException e) {
                        Log.warn(e.getMessage());
                    } else {
                        Log.error("Error deleting sector with id: " + sectorId, ex);
                    }
                });
    }
}
