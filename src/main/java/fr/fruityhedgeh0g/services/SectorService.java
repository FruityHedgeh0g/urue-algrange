package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateEntityException;
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

import java.util.*;
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

    @Transactional
    public Try<SectorDto> createSector(@NotNull SectorDto sectorDto) {
        return Try.of(() -> {
            Log.debug("Searching for already existing sector with name: " + sectorDto.getName());
            if (sectorRepository.existsByName(sectorDto.getName())) {throw new DuplicateEntityException("A sector with the same name already exists");}
            if (sectorDto.getGroups().isEmpty()) {throw new IllegalArgumentException("A sector must contain at least one group at creation");}

            Log.debug("Checking if all groups exist and doesn't belong to another sector");
            SectorEntity sectorEntity = sectorMapper.toEntity(sectorDto);
            sectorEntity.setGroups(sectorEntity.getGroups()
                    .stream()
                    .map(grp -> groupService.getGroupEntityById(grp.getGroupId()).getOrElseThrow(e -> new IllegalArgumentException("Group "+grp.getGroupId()+" does not exist")))
                    .peek(grp -> {
                        if (grp.getSector() != null) {
                            throw new IllegalArgumentException("A group can only belong to one sector. Group " + grp.getGroupId() + " already belongs to a sector");
                        }
                    })
                    .peek(group -> group.setSector(sectorEntity))
                    .collect(Collectors.toSet()));

            Log.debug("Creating sector: " + sectorDto.getName());

            sectorRepository.persist(sectorEntity);


            Log.debug("Sector created, retrieving up-to-date sector infos: " + sectorEntity.getSectorId());
            return sectorMapper.toDto(sectorEntity);
        }).onFailure(e -> {
            if (e instanceof DuplicateEntityException) {
                Log.warn("Sector already exists: " + sectorDto.getName());
            }else {
                Log.error("Error creating sector with name: " + sectorDto.getName(), e);
            }
        });
    }

    @Transactional
    public Try<SectorDto> assignGroupToSector(@NotNull UUID sectorId, @NotNull UUID groupId) {
        return Try.of(()-> {
            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
                    .orElseThrow(() -> new NoSuchElementException("Sector not found"));
            GroupEntity groupEntity = groupService.getGroupEntityById(groupId)
                    .getOrElseThrow(e -> new NoSuchElementException("Group not found"));

            if (groupEntity.getSector() != null) throw new IllegalArgumentException("Group already belongs to a sector");

            sectorEntity.addGroup(groupEntity);
            return sectorMapper.toDto(sectorEntity);
        }).onFailure(e -> {
            if (e instanceof NoSuchElementException) {
                Log.warn("Group not found: " + groupId);
            }else {
                Log.error("Error assigning group with id: " + groupId + " to sector with id: " + sectorId, e);
            }
                }
        );
    }

    //TODO : Développer l'update
    public Try<SectorDto> updateSector(@NotNull SectorDto sectorDto) {
        Log.info("Updating sector: " + sectorDto.getSectorId());
        return Try.of(() -> {
            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorDto.getSectorId())
                    .orElseThrow(() -> new NoSuchElementException("Sector not found"));

            sectorEntity = sectorMapper.updateEntityFromDto(sectorEntity,sectorDto);

            return sectorMapper.toDto(sectorRepository
                    .findByIdOptional(sectorEntity.getSectorId())
                    .orElseThrow(() -> new NoSuchElementException("Sector not found")));
        }).onFailure(ex -> {
            Log.error("Error updating sector with id: " + sectorDto.getSectorId(), ex);
        });
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    public boolean deleteSector(@NotNull UUID sectorId) {
        Log.info("Deleting sector with id: " + sectorId);
        return Try.run(() -> sectorRepository.deleteById(sectorId))
                .onFailure(e -> Log.error("Error deleting sector with id: " + sectorId, e)).isSuccess();
    }
}
