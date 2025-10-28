package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateEntityException;
import fr.fruityhedgeh0g.model.dtos.GroupDto;
import fr.fruityhedgeh0g.model.entities.GroupEntity;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@ApplicationScoped
public class GroupService {
    @Inject
    GroupRepository groupRepository;

    @Inject
    GroupMapper groupMapper;

    @Transactional
    public Try<List<GroupDto>> getAllGroups(){
        Log.info("Getting all groups");
        return Try.of(() -> groupRepository
                .findAll()
                    .stream()
                    .map(groupMapper::toDto)
                    .toList())
                    .onFailure(e -> Log.error("Error getting all groups", e));
    }

    public Try<GroupDto> getGroupById(@NotNull UUID groupId){
        Log.info("Getting group with id: " + groupId);
        return Try.of(() -> groupRepository
                        .findByIdOptional(groupId)
                        .orElseThrow(NoSuchElementException::new))
                .map(groupMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Group not found: " + groupId);
                    }else {
                        Log.error("Error getting group with id: " + groupId, e);
                    }
                });
    }

    public Try<Set<GroupDto>> getGroupsByIds(@NotNull Set<UUID> groupIds){
      Log.info("Getting groups with ids: " + groupIds);

      return Try.of(() -> groupRepository.findByIds(groupIds)
              .stream()
              .map(groupMapper::toDto)
              .collect(Collectors.toSet()))
              .onFailure(e -> {
                  Log.error("Error getting groups with ids: " + groupIds, e);
              });
    };

    @Transactional
    public Try<GroupDto> createGroup(@NotNull GroupDto groupDto){
        return Try.of(() -> {
            Log.debug("Searching for already existing group with name: " + groupDto.getName());
            if (groupRepository.existsByName(groupDto.getName())) {
                throw new DuplicateEntityException();
            }

            Log.debug("Creating user: " + groupDto.getGroupId());
            GroupEntity groupEntity = groupMapper.toEntity(groupDto);
            groupRepository.persist(groupEntity);

            Log.debug("Group created, retrieving up-to-date group infos: " + groupEntity.getGroupId());
            return groupMapper.toDto(
                    groupRepository
                            .findByIdOptional(groupEntity.getGroupId())
                            .orElseThrow(NoSuchElementException::new)
            );
        }).onFailure(e -> {
            if (e instanceof DuplicateEntityException) {
                Log.warn("Group already exists: " + groupDto.getName());
            }else {
                Log.error("Error creating group with name: " + groupDto.getName(), e);
            }
        });
    }

    //TODO : Développer l'update
    public Try<GroupDto> updateGroup(@NotNull GroupDto groupDto){
        Log.info("Updating group: " + groupDto.getGroupId());
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    public void deleteGroup(@NotNull UUID groupId){
        Log.info("Deleting group with id: " + groupId);
        Try.of(() -> groupRepository.deleteById(groupId))
            .onFailure(e -> Log.error("Error deleting group with id: " + groupId, e));
    }




}
