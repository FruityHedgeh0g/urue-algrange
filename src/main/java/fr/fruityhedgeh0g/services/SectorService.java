package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidInputException;
import fr.fruityhedgeh0g.exceptions.ResourceNotFoundException;
import fr.fruityhedgeh0g.model.dtos.SectorDto;
import fr.fruityhedgeh0g.model.entities.GroupEntity;
import fr.fruityhedgeh0g.model.entities.SectorEntity;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import fr.fruityhedgeh0g.utilities.mappers.SectorMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@ApplicationScoped
public class SectorService {
    @Inject
    SectorRepository sectorRepository;

    @Inject
    GroupService groupService;

    @Inject
    SectorMapper sectorMapper;

    @Inject
    GroupMapper groupMapper;

    @Inject
    GroupRepository groupRepository;

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
    public Try<SectorDto> getSectorById(@NotNull UUID sectorId) {
        Log.info("Getting sector with id: " + sectorId);
        return Try.of(() -> sectorRepository
                        .findByIdOptional(sectorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Sector not found:" + sectorId)))
                .map(sectorMapper::toDto)
                .onFailure(ex -> {
                    if (Objects.requireNonNull(ex) instanceof NoSuchElementException e) {
                        Log.warn(e.getMessage());
                    } else {
                        Log.error("Error getting sector with id: " + sectorId, ex);
                    }
                });
    }

    @Transactional
    public Try<SectorDto> createSector(@NotNull SectorDto sectorDto) {
        return Try.of(() -> {
            Log.debug("Searching for already existing sector with name: " + sectorDto.getName());
            if (sectorRepository.existsByName(sectorDto.getName())) {throw new DuplicateResourceException("A sector with the same name already exists");}
            if (sectorDto.getGroups().isEmpty()) {throw new InvalidInputException("A sector must contain at least one group at creation");}

            Log.debug("Checking if all groups exist and doesn't belong to another sector");
            SectorEntity sectorEntity = sectorMapper.toEntity(sectorDto);
            sectorEntity.setGroups(sectorEntity.getGroups()
                    .stream()
                    .map(grp -> groupService.getGroupEntityById(grp.getGroupId()).getOrElseThrow(e -> new ResourceNotFoundException("Group "+grp.getGroupId()+" does not exist")))
                    .peek(grp -> {
                        if (grp.getSector() != null) {
                            throw new DuplicateResourceException("A group can only belong to one sector. Group " + grp.getGroupId() + " already belongs to a sector");
                        }
                    })
                    .peek(group -> group.setSector(sectorEntity))
                    .collect(Collectors.toSet()));

            Log.debug("Creating sector: " + sectorDto.getName());

            sectorRepository.persist(sectorEntity);


            Log.debug("Sector created, retrieving up-to-date sector infos: " + sectorEntity.getSectorId());
            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            switch (ex) {
                case ResourceNotFoundException e -> Log.warn(e.getMessage());
                case DuplicateResourceException e -> Log.warn(e.getMessage());
                default -> Log.error("Error creating sector with name: " + sectorDto.getName(), ex);
            }
        });
    }

    @Transactional
    public Try<SectorDto> assignGroupToSector(@NotNull UUID sectorId, @NotNull UUID groupId) {
        return Try.of(()-> {
            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sector not found: " + sectorId));
            GroupEntity groupEntity = groupService.getGroupEntityById(groupId)
                    .getOrElseThrow(e -> new ResourceNotFoundException("Group not found: "+groupId));

            if (groupEntity.getSector() != null) throw new DuplicateResourceException("Group already belongs to a sector");

            sectorEntity.addGroup(groupEntity);
            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            switch (ex) {
                case ResourceNotFoundException e -> Log.warn(e.getMessage());
                case DuplicateResourceException e -> Log.warn(e.getMessage());
                default -> Log.error("Error assigning group to sector with id: " + sectorId, ex);
            }
        });
    }

    @Transactional
    public Try<SectorDto> unassignGroupToSector(@NotNull UUID sectorId, @NotNull UUID groupId) {
        return Try.of(() -> sectorRepository.findByIdOptional(sectorId)
                .orElseThrow(() -> new ResourceNotFoundException("Sector not found:" + sectorId)))
                .peek(sector -> {
                    if (sector.getGroups().size() == 1) throw new InvalidInputException("Sector must contain at least one group");

                    GroupEntity groupEntity = groupService.getGroupEntityById(groupId)
                            .getOrElseThrow(e -> new ResourceNotFoundException("Group not found:" +groupId ));
                    sector.removeGroup(groupEntity);
                }).map(sectorMapper::toDto)
                .onFailure(ex -> {
                    switch (ex) {
                        case ResourceNotFoundException e -> Log.warn(e.getMessage());
                        case InvalidInputException e -> Log.warn(e.getMessage());
                        default -> Log.error("Error unassigning group to sector with id: " + sectorId, ex);
                    }
                });
    }

    @Transactional
    public Try<SectorDto> updateSector(@NotNull SectorDto sectorDto) {
        return Try.of(() -> {
            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorDto.getSectorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sector not found:" + sectorDto.getSectorId()));

            sectorMapper.updateEntityFromDto(sectorEntity,sectorDto);

            return sectorMapper.toDto(sectorEntity);
        }).onFailure(ex -> {
            if (Objects.requireNonNull(ex) instanceof ResourceNotFoundException e) {
                Log.warn(e.getMessage());
            } else {
                Log.error("Error updating sector with id: " + sectorDto.getSectorId(), ex);
            }
        });
    }

    @Transactional
    public void deleteSector(@NotNull UUID sectorId) {
        Try.of(() -> sectorRepository.findByIdOptional(sectorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Sector not found:" + sectorId)))
                .peek(sector -> sectorRepository.delete(sector))
                .onFailure(ex -> {
                    if (Objects.requireNonNull(ex) instanceof ResourceNotFoundException e) {
                        Log.warn(e.getMessage());
                    } else {
                        Log.error("Error deleting sector with id: " + sectorId, ex);
                    }
                });
    }
}
