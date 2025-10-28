package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateEntityException;
import fr.fruityhedgeh0g.model.dtos.GroupDto;
import fr.fruityhedgeh0g.model.dtos.SectorDto;
import fr.fruityhedgeh0g.model.entities.GroupEntity;
import fr.fruityhedgeh0g.model.entities.SectorEntity;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.repositories.SectorRepository;
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
            if (sectorRepository.existsByName(sectorDto.getName())) {
                throw new DuplicateEntityException("A sector with name " + sectorDto.getName() + " already exists");
            }

            if (sectorDto.getGroups().isEmpty()) {
                throw new IllegalArgumentException("A sector must belong to at least one group");
            }

            Log.debug("Checking if all groups exist");
            Set<GroupDto> groups = groupService.getGroupsByIds(
                        sectorDto.getGroups()
                            .stream().map(GroupEntity::getGroupId)
                            .collect(Collectors.toSet())
                    ).get();


            if (groups.size() != sectorDto.getGroups().size()) throw new IllegalArgumentException("Some groups do not exist");

            Log.debug("Checking if all groups belong to the same sector");
            groups.forEach(group -> {
                if (group.getSector() != null) {
                    throw new IllegalArgumentException("A group can only belong to one sector. Group " + group.getGroupId() + " already belongs to a sector");
                }
            });

            Log.debug("Creating sector: " + sectorDto.getName());
            SectorEntity sectorEntity = sectorMapper.toEntity(sectorDto);
            sectorRepository.persist(sectorEntity);

            Log.debug("Sector created, retrieving up-to-date sector infos: " + sectorEntity.getSectorId());
            return sectorMapper.toDto(
                    sectorRepository
                            .findByIdOptional(sectorEntity.getSectorId())
                            .orElseThrow(NoSuchElementException::new)
            );
        }).onFailure(e -> {
            if (e instanceof DuplicateEntityException) {
                Log.warn("Sector already exists: " + sectorDto.getName());
            }else {
                Log.error("Error creating sector with name: " + sectorDto.getName(), e);
            }
        });
    }

    @Transactional
    public Try<SectorDto> assignGroup(@NotNull UUID sectorId, @NotNull UUID groupId) {
        return Try.of(()-> {
            if (!sectorRepository.existsById(sectorId)) throw new NoSuchElementException("Sector not found");

            GroupDto groupDto = groupService.getGroupById(groupId)
                    .getOrElseThrow(e -> new NoSuchElementException("Group not found"));

            if (groupDto.getSector() != null) throw new IllegalArgumentException("Group already belongs to a sector");

            //todo : ajouter le modification du groupe pour ajout uuid secteur

            return sectorMapper.toDto(sectorRepository.findByIdOptional(sectorId).get());
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
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    public void deleteSector(@NotNull UUID sectorId) {
        Log.info("Deleting sector with id: " + sectorId);
        Try.run(() -> sectorRepository.deleteById(sectorId))
                .onFailure(e -> Log.error("Error deleting sector with id: " + sectorId, e));
    }
}
