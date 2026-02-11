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
    @Identifier( "serviceImpl")
    UserService userServiceImpl;

    @Inject
    GroupMapper groupMapper;

    //Todo faire un checkup du service pour assurer la cohérence des méthodes

    @Override
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

    @Override
    public Boolean internalExistsById( UUID groupId){
        Log.info("Checking if group exists with id: " + groupId);
        return groupRepository.existsById(groupId);
    }

    @Override
    public GroupEntity internalGetEntityById(UUID groupId){
        Log.info("Getting group with id: " + groupId);
        return groupRepository.findByIdOptional(groupId).orElseThrow(() ->
                new UnknownResourceException("Group not found: " + groupId));

    }

    @Override
    @Transactional
    public Try<GroupDto> getGroupById( UUID groupId){
        Log.info("Getting group with id: " + groupId);
        return Try.of(() -> internalGetEntityById(groupId))
                .map(groupMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof UnknownResourceException) {
                        Log.warn(e.getMessage());
                    }else {
                        Log.error("Error getting group with id: " + groupId, e);
                    }
                });
    }

    @Override
    public Set<GroupEntity> internalGetBySectorId(UUID sectorId) throws UnknownResourceException{
        Log.info("Getting all groups");
        return groupRepository
                .findBySector(sectorId)
                .orElseThrow(() -> new
                        UnknownResourceException("No group found for sector: " + sectorId )
                );

    }


    @Override
    @Transactional
    public Try<Set<GroupDto>> getGroupsBySectorId( UUID sectorId){
        Log.info("Getting all groups for sector with id: " + sectorId);
        return Try.of(() -> internalGetBySectorId(sectorId))
                .map(groupEntities -> groupEntities
                        .stream()
                        .map(groupMapper::toDto)
                        .collect(Collectors.toSet()))
                .onFailure(e -> Log.errorf(e ,"A mapping error occurred for sector id: %s" + sectorId));
    }

    @Override
    @Transactional
    public Try<GroupDto> createGroup( GroupDto groupDto){
        Log.infof("Creating group with name: %s" , groupDto.getName());
        return Try.of(() -> {
            if (groupRepository.existsByName(groupDto.getName()))
                throw new DuplicateResourceException("Group already exists: " + groupDto.getName() );

            GroupEntity groupEntity = groupMapper.toEntity(groupDto);

            groupRepository.persist(groupEntity);

            return groupMapper.toDto(groupEntity);
        }).onFailure(e -> {
            if (e instanceof DuplicateResourceException) {
                Log.warnf("Group already exists: %s" , groupDto.getName());
            }else {
                Log.errorf(e, "Error creating group with name: %s" , groupDto.getName() );
            }
        });
    }

    @Override
    @Transactional
    public Try<GroupDto> updateGroup( GroupDto groupDto){
        Log.infof("Updating group with id: %s" , groupDto.getGroupId());
        return Try.of(() -> {
                    Log.debugf("Checking if group with id: %s exists" , groupDto.getGroupId());
                    GroupEntity group = internalGetEntityById(groupDto.getGroupId());

                    Log.debugf("Checking if group with name: %s already exists" , groupDto.getName());
                    if (groupRepository.findByName(groupDto.getName()).stream().anyMatch(e -> !e.getGroupId().equals(groupDto.getGroupId())))
                        throw new DuplicateResourceException("Group already exists: " + groupDto.getName());

                    groupMapper.partialDtoToEntity(group, groupDto);
                    return groupMapper.toDto(group);
                }).onFailure(ex -> {
                    switch(ex) {
                        case UnknownResourceException e -> Log.warn(e.getMessage());
                        case DuplicateResourceException e -> Log.warn(e.getMessage());
                        default -> Log.errorf(ex,"Error updating group with id: %s" , groupDto.getGroupId() );
                    }
                });
    }

    @Override
    @Transactional
    public Try<Void> deleteGroup( UUID groupId){
        Log.infof("Deleting group with id: %s" , groupId);
        return Try.run(() -> {
            Log.debugf("Checking if group with id: %s exists" , groupId);
            GroupEntity group = internalGetEntityById(groupId);

            Log.debugf("Removing all members from group with id: %s" , groupId);
            group.getMembers().forEach(group::removeMember);

            Log.debugf("Deleting group with id: %s" , groupId);
            groupRepository.delete(group);
        }).onFailure(ex -> {
            switch(ex) {
                case UnknownResourceException e -> Log.warn(e.getMessage());
                default -> Log.errorf(ex,"Error deleting group with id: %s" , groupId );
            }

        });
    }

    @Override
    @Transactional
    public Try<GroupDto> assignUserToGroup( UUID userId,  UUID groupId){
        Log.debugf("Assigning user with id: %s to group with id: %s" , userId, groupId);
        return Try.of(() -> {
                    GroupEntity group = internalGetEntityById(groupId);
                    UserEntity userEntity = userServiceImpl.internalGetUserById(userId);

                    group.addMember(userEntity);
                    return groupMapper.toDto(group);
                }).onFailure(ex -> {
                    if (ex instanceof UnknownResourceException e) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.error("Error assigning user to group with id: " + groupId, ex);
                    }
                });
    }

    @Override
    @Transactional
    public Try<GroupDto> unassignUserFromGroup( UUID userId,  UUID groupId){
        Log.debugf("Unassigning user with id: %s from group with id: %s" , userId, groupId);
        return Try.of(() -> {
                    GroupEntity group = internalGetEntityById(groupId);
                    UserEntity userEntity = userServiceImpl.internalGetUserById(userId);
                    group.removeMember(userEntity);
                    return groupMapper.toDto(group);
                }).onFailure(ex -> {
                    switch (ex) {
                        case UnknownResourceException e -> Log.warn(e.getMessage());
                        case InvalidInputException e -> Log.warn(e.getMessage());
                        default -> Log.error("Error unassigning user from group with id: " + groupId, ex);
                    }
                });
    }


}
