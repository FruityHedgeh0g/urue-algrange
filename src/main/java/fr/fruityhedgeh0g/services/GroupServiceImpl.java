package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidInputException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.experimental.PackagePrivate;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class GroupServiceImpl implements GroupService {
    @Inject
    GroupRepository groupRepository;

    @Inject
    UserService userServiceImpl;

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

    @Transactional
    @PackagePrivate
    Try<Boolean> existsGroupById( UUID groupId){
        Log.info("Checking if group exists with id: " + groupId);
        return Try.of(() -> groupRepository.existsById(groupId))
                .onFailure(e -> Log.error("Error checking if group exists with id: " + groupId, e));
    }

    public Try<GroupEntity> getInternalEntityById( UUID groupId){
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

    @Transactional
    public Try<GroupDto> getGroupById( UUID groupId){
        return Try.of(() -> groupRepository
                        .findByIdOptional(groupId)
                        .orElseThrow(() -> new UnknownResourceException("Group not found:" + groupId)))
                .map(groupMapper::toDto)
                .onFailure(ex -> {
                    if (Objects.requireNonNull(ex) instanceof UnknownResourceException e) {
                        Log.warn(e.getMessage());
                    } else {
                        Log.error("Error getting sector with id: " + groupId, ex);
                    }
                });
    }

    @PackagePrivate
    Try<Set<GroupEntity>> getInternalEntitiesBySectorId( UUID sectorId){
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

    @Transactional
    public Try<Set<GroupDto>> getGroupsBySectorId( UUID sectorId){
        return getInternalEntitiesBySectorId(sectorId)
                .map(groupEntities -> groupEntities
                        .stream()
                        .map(groupMapper::toDto)
                        .collect(Collectors.toSet()))
                .onFailure(e -> Log.error("A mapping error occurred: " + sectorId, e));
    }

    @Transactional
    public Try<GroupDto> createGroup( GroupDto groupDto){
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
    public Try<GroupDto> updateGroup( GroupDto groupDto){
        Log.debug("Updating group: " + groupDto.getGroupId());
        return Try.of(() -> groupRepository.findByIdOptional(groupDto.getGroupId())
                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupDto.getGroupId())))
                .peek(group -> groupMapper.partialDtoToEntity(group, groupDto))
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
    public Try<Void> deleteGroup( UUID groupId){
        return null;
    }

    @Transactional
    public Try<GroupDto> assignUserToGroup( UUID userId,  UUID groupId){
        return Try.of(() -> groupRepository.findByIdOptional(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupId)))
                .peek(group -> {
                    UserEntity userEntity = userServiceImpl.getInternalUserById(userId)
                            .getOrElseThrow(() -> new UnknownResourceException("User not found: " + userId));

                    group.addMember(userEntity);
                }).map(groupMapper::toDto)
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException e) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.error("Error assigning user to group with id: " + groupId, ex);
                    }
                });
    }

    @Transactional
    public Try<GroupDto> unassignUserFromGroup( UUID userId,  UUID groupId){
        return Try.of(() -> groupRepository.findByIdOptional(groupId)
                        .orElseThrow(() -> new UnknownResourceException("Group not found:" + groupId)))
                .peek(group -> {
                    UserEntity userEntity = userServiceImpl.getInternalUserById(userId)
                            .getOrElseThrow(e -> new UnknownResourceException("User not found:" +userId ));
                    group.removeMember(userEntity);
                }).map(groupMapper::toDto)
                .onFailure(ex -> {
                    switch (ex) {
                        case UnknownResourceException e -> Log.warn(e.getMessage());
                        case InvalidInputException e -> Log.warn(e.getMessage());
                        default -> Log.error("Error unassigning user from group with id: " + groupId, ex);
                    }
                });
    }


}
