package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.model.dtos.GroupDto;
import fr.fruityhedgeh0g.model.entities.GroupEntity;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.experimental.PackagePrivate;

import java.util.List;
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

    @PackagePrivate
    Try<GroupEntity> getGroupEntityById(@NotNull UUID groupId){
        Log.info("Getting group with id: " + groupId);
        return Try.of(() -> groupRepository
                        .findByIdOptional(groupId)
                        .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupId)))
                .onFailure(e -> {
                    if (e instanceof UnknownResourceException) {
                        Log.warn("Group not found: " + groupId);
                    }else {
                        Log.error("Error getting group with id: " + groupId, e);
                    }
                });
    }

    public Try<GroupDto> getGroupById(@NotNull UUID groupId){
         return getGroupEntityById(groupId).map(groupMapper::toDto)
                 .onFailure(e -> Log.error("A mapping error occurred: " + groupId, e));
    }

    @PackagePrivate
    Try<Set<GroupEntity>> getGroupsEntitiesBySectorId(@NotNull UUID sectorId){
        Log.info("Getting all groups");
        return Try.of(() -> groupRepository
                        .findBySector(sectorId).orElseThrow(() -> new UnknownResourceException("No group found for sector: " + sectorId )))
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException e) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.error("Error getting all groups", ex);
                    }
                });
    }

    public Try<Set<GroupDto>> getGroupsBySectorId(@NotNull UUID sectorId){
        return getGroupsEntitiesBySectorId(sectorId)
                .map(groupEntities -> groupEntities
                        .stream()
                        .map(groupMapper::toDto)
                        .collect(Collectors.toSet()))
                .onFailure(e -> Log.error("A mapping error occurred: " + sectorId, e));
    }

    @Transactional
    public Try<GroupDto> createGroup(@NotNull GroupDto groupDto){
        return Try.of(() -> {
            if (groupRepository.existsByName(groupDto.getName())) throw new DuplicateResourceException("Group already exists: " + groupDto.getName() );

            GroupEntity groupEntity = groupMapper.toEntity(groupDto);

            groupRepository.persist(groupEntity);

            return groupMapper.toDto(groupEntity);
        }).onFailure(e -> {
            if (e instanceof DuplicateResourceException) {
                Log.warn("Group already exists: " + groupDto.getName());
            }else {
                Log.error("Error creating group with name: " + groupDto.getName(), e);
            }
        });
    }

    @Transactional
    public Try<GroupDto> updateGroup(@NotNull GroupDto groupDto){
        Log.debug("Updating group: " + groupDto.getGroupId());
        return Try.of(() -> groupRepository.findByIdOptional(groupDto.getGroupId())
                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupDto.getGroupId())))
                .peek(group -> groupMapper.updateEntityFromDto(group, groupDto))
                .map(groupMapper::toDto)
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.error("Error updating group with id: " + groupDto.getGroupId(), ex);
                    }
                });
    }

    @Transactional
    public void deleteGroup(@NotNull UUID groupId){
        Log.info("Deleting group with id: " + groupId);
        Try.of(() -> groupRepository.findByIdOptional(groupId)
                    .orElseThrow(() -> new UnknownResourceException("Group not found: " +groupId)))
                .peek(group -> groupRepository.delete(group))
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException e) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.error("Error deleting group with id: " + groupId, ex);
                    }
                });
    }




}
