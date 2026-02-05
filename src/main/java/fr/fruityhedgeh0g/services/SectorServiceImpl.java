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
        Log.infof("Getting sector with id: %id", sectorId);
        return Try.of(() -> sectorRepository
                        .findByIdOptional(sectorId)
                        .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorId)))
                .map(sectorMapper::toDto)
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException e) {
                        Log.warn(e.getMessage());
                    } else {
                        Log.errorf(ex, "Error getting sector with id: %id", sectorId);
                    }
                });
    }

    @Transactional
    public Try<SectorDto> createSector( SectorDto sectorDto) {
        Log.infof("Creating sector: %u", sectorDto);
        return Try.of(() -> {
            Log.debugf("Checking if sector with name: %s already exists", sectorDto.getName());
            if (sectorRepository.existsByName(sectorDto.getName())) {throw new DuplicateResourceException("A sector with the same name already exists");}
            SectorEntity sectorEntity = sectorMapper.toEntity(sectorDto);

            Log.debug("Persisting new sector: " + sectorEntity.getSectorId());
            sectorRepository.persist(sectorEntity);
            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            switch (ex) {
                case UnknownResourceException e -> Log.warn(e.getMessage());
                case InvalidInputException e -> Log.warn(e.getMessage());
                default -> Log.errorf(ex, "Error creating sector: %u", sectorDto);
            }
        });
    }

    @Transactional
    public Try<SectorDto> assignGroupToSector( UUID sectorId,  UUID groupId) {
        Log.infof("Assigning group with id: %id to sector with id: %id", groupId, sectorId);
        return Try.of(()-> {
            Log.debugf("Checking if sector with id: %id exists", sectorId);
            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
                    .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));

            Log.debugf("Checking if group with id: %id exists", groupId);
            GroupEntity groupEntity = groupService.getInternalEntityById(groupId)
                    .getOrElseThrow(e -> {throw new UnknownResourceException("Group not found: "+groupId);});

            Log.debugf("Checking if group with id: %id is already assigned to a sector", groupId);
            if (groupEntity.getSector() != null) throw new InvalidInputException("Group already belongs to a sector");

            sectorEntity.addGroup(groupEntity);
            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            switch (ex) {
                case UnknownResourceException e -> Log.warn(e.getMessage());
                default -> Log.errorf(ex, "Error assigning group with id: %id to sector with id: %id", groupId, sectorId);
            }
        });
    }

    //TODO: check si le groupe est bien dans ce secteur avant de le retirer
    @Transactional
    public Try<SectorDto> unassignGroupFromSector( UUID sectorId,  UUID groupId) {
        Log.infof("Unassigning group with id: %id from sector with id: %id", groupId, sectorId);
        return Try.of(() -> {
            Log.debugf("Checking if sector with id: %id exists", sectorId);
            return sectorRepository.findByIdOptional(sectorId)
                .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorId));
        }).peek(sector -> {
                    Log.debugf("Checking if group with id: %id exists", groupId);
                    GroupEntity groupEntity = groupService.getInternalEntityById(groupId)
                            .getOrElseThrow(e -> new UnknownResourceException("Group not found:" +groupId ));

                    sector.removeGroup(groupEntity);
                }).map(sectorMapper::toDto)
                .onFailure(ex -> {
                    switch (ex) {
                        case UnknownResourceException e -> Log.warn(e.getMessage());
                        default -> Log.errorf(ex, "Error unassigning group with id: %id from sector with id: %id", groupId, sectorId);
                    }
                });
    }

    @Transactional
    public Try<SectorDto> updateSector( SectorDto sectorDto) {
        Log.infof("Updating sector: %u", sectorDto);
        return Try.of(() -> {
            Log.debugf("Checking if sector with id: %id exists", sectorDto.getSectorId());
            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorDto.getSectorId())
                    .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorDto.getSectorId()));

            Log.debug("Updating sector info");
            sectorMapper.partialDtoToEntity(sectorEntity,sectorDto);

            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            if (Objects.requireNonNull(ex) instanceof UnknownResourceException e) {
                Log.warn(e.getMessage());
            } else {
                Log.errorf(ex, "Error updating sector: %u", sectorDto);
            }
        });
    }

    @Transactional
    public Try<Void> deleteSector( UUID sectorId) {
        Log.infof("Deleting sector with id: %id", sectorId);
        return Try.run(() -> {
            Log.debugf("Checking if sector with id: %id exists", sectorId);
            SectorEntity sector =sectorRepository.findByIdOptional(sectorId)
                        .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorId));
            Log.debugf("Deleting sector with id: %id", sectorId);
            sectorRepository.delete(sector);
        }).onFailure(ex -> {
            if (Objects.requireNonNull(ex) instanceof UnknownResourceException e) {
                Log.warn(e.getMessage());
            } else {
                Log.errorf(ex, "Error deleting sector with id: %id", sectorId);
            }
        });
    }
}
