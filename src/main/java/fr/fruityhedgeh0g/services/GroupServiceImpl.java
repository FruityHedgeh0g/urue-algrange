package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.*;

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

    @Override
    public List<GroupDto> listAll() {
        return groupRepository.listAll()
                .stream()
                .map(groupMapper::toDto)
                .toList();
    }

    @Override
    public Optional<GroupDto> getById(UUID groupId) {
        return null;
    }

    @Override
    public GroupDto create(GroupDto groupDto) {
        return null;
    }

    @Override
    public GroupDto update(GroupDto groupDto) {
        return null;
    }

    @Override
    public void delete(UUID groupId) {

    }

//    //Todo faire un checkup du service pour assurer la cohérence des méthodes
//
//    @Override
//    @Transactional
//    public Try<List<GroupDto>> getAllGroups(){
//        Log.info("Getting all groups");
//        return Try.of(() -> groupRepository
//                        .findAll()
//                        .stream()
//                        .map(groupMapper::toDto)
//                        .toList())
//                .onFailure(e -> Log.error("Error getting all groups", e));
//    }
//
//    @Override
//    public Try<Boolean> internalExistsById(UUID groupId){
//        Log.info("Checking if group exists with id: " + groupId);
//        return Try.of(() -> groupRepository.existsById(groupId));
//    }
//
//    @Override
//    public Try<GroupEntity> internalGetEntityById(UUID groupId){
//        Log.info("Getting group with id: " + groupId);
//        return Try.of(() -> groupRepository.findByIdOptional(groupId).orElseThrow(() ->
//                new UnknownResourceException("Group not found: " + groupId)));
//
//    }
//
//    @Override
//    @Transactional
//    public Try<GroupDto> getGroupById( UUID groupId){
//        Log.info("Getting group with id: " + groupId);
//        return Try.of(() -> internalGetEntityById(groupId).getOrElseThrow(ex -> ex))
//                .map(groupMapper::toDto)
//                .onFailure(e -> {
//                    if (e instanceof UnknownResourceException) {
//                        Log.warn(e.getMessage());
//                    }else {
//                        Log.error("Error getting group with id: " + groupId, e);
//                    }
//                });
//    }
//
//    @Override
//    public Try<Set<GroupEntity>> internalGetBySectorId(UUID sectorId) throws UnknownResourceException{
//        Log.info("Getting all groups");
//        return Try.of(() -> groupRepository.findBySector(sectorId).orElseThrow(() ->
//                        new UnknownResourceException("No group found for sector: " + sectorId )));
//    }
//
//
//    @Override
//    @Transactional
//    public Try<Set<GroupDto>> getGroupsBySectorId( UUID sectorId){
//        Log.info("Getting all groups for sector with id: " + sectorId);
//        return Try.of(() -> internalGetBySectorId(sectorId).getOrElseThrow(ex -> ex))
//                .map(groupEntities -> groupEntities
//                        .stream()
//                        .map(groupMapper::toDto)
//                        .collect(Collectors.toSet()))
//                .onFailure(e -> Log.errorf(e ,"A mapping error occurred for sector id: %s" + sectorId));
//    }
//
//    @Override
//    @Transactional
//    public Try<GroupDto> createGroup( GroupDto groupDto){
//        Log.infof("Creating group with name: %s" , groupDto.getName());
//        return Try.of(() -> {
//            Log.debugf("Checking if group with name: %s already exists" , groupDto.getName());
//            if (groupRepository.existsByName(groupDto.getName()))
//                throw new DuplicateResourceException("Group already exists: " + groupDto.getName() );
//
//            GroupEntity groupEntity = groupMapper.toEntity(groupDto);
//
//            groupRepository.persist(groupEntity);
//
//            return groupMapper.toDto(groupEntity);
//        }).onFailure(e -> {
//            if (e instanceof DuplicateResourceException) {
//                Log.warnf("Group already exists: %s" , groupDto.getName());
//            }else {
//                Log.errorf(e, "Error creating group with name: %s" , groupDto.getName() );
//            }
//        });
//    }
//
//    @Override
//    @Transactional
//    public Try<GroupDto> updateGroup( GroupDto groupDto){
//        Log.infof("Updating group with id: %s" , groupDto.getGroupId());
//        return Try.of(() -> {
//                    Log.debugf("Checking if group with id: %s exists and retrieve it" , groupDto.getGroupId());
//                    GroupEntity group = internalGetEntityById(groupDto.getGroupId()).getOrElseThrow(ex -> ex);
//
//                    Log.debugf("Checking if group with name: %s already exists" , groupDto.getName());
//                    if (groupRepository.existsByName(groupDto.getName()) && !groupDto.getName().equals(group.getName()))
//                        throw new DuplicateResourceException("Group already exists: " + groupDto.getName());
//
//                    groupMapper.partialDtoToEntity(group, groupDto);
//                    return groupMapper.toDto(group);
//                }).onFailure(ex -> {
//                    switch(ex) {
//                        case UnknownResourceException e -> Log.warn(e.getMessage());
//                        case DuplicateResourceException e -> Log.warn(e.getMessage());
//                        default -> Log.errorf(ex,"Error updating group with id: %s" , groupDto.getGroupId() );
//                    }
//                });
//    }
//
//    @Override
//    @Transactional
//    public Try<Void> deleteGroup( UUID groupId){
//        Log.infof("Deleting group with id: %s" , groupId);
//        return Try.run(() -> {
//            Log.debugf("Checking if group with id: %s exists and retrieve it" , groupId);
//            GroupEntity group = internalGetEntityById(groupId).getOrElseThrow(ex -> ex);
//
//            Log.debugf("Removing group from sector with id: %s" , group.getSector().getSectorId());
//            group.getSector().removeGroup(group);
//
//            Log.debugf("Removing all members from group with id: %s" , groupId);
//            group.getMembers().forEach(group::removeMember);
//
//            Log.debugf("Deleting group with id: %s" , groupId);
//            groupRepository.delete(group);
//        }).onFailure(ex -> {
//            switch(ex) {
//                case UnknownResourceException e -> Log.warn(e.getMessage());
//                default -> Log.errorf(ex,"Error deleting group with id: %s" , groupId );
//            }
//
//        });
//    }
//
//    @Override
//    @Transactional
//    public Try<GroupDto> assignUserToGroup( UUID userId,  UUID groupId){
//        Log.debugf("Assigning user with id: %s to group with id: %s" , userId, groupId);
//        return Try.of(() -> {
//                    Log.debugf("Checking if user with id: %s exists and retrieve it" , userId);
//                    GroupEntity group = internalGetEntityById(groupId).getOrElseThrow(ex -> ex);
//
//                    if (group.getMembers().stream().anyMatch(e -> e.getUserId().equals(userId)))
//                        throw new DuplicateResourceException("User already belongs to this group");
//
//                    Log.debugf("Checking if user with id: %s exists and retrieve it" , userId);
//                    UserEntity userEntity = userServiceImpl.internalGetUserById(userId).getOrElseThrow(ex -> ex);
//
//                    Log.debugf("Checking if user with id: %s is already assigned to a group" , userId);
//                    if (userEntity.getGroup() != null)
//                        throw new DuplicateResourceException("User already belongs to a group");
//
//                    group.addMember(userEntity);
//                    return groupMapper.toDto(group);
//                }).onFailure(ex -> {
//                    if (ex instanceof UnknownResourceException e) {
//                        Log.warn(ex.getMessage());
//                    }else {
//                        Log.error("Error assigning user to group with id: " + groupId, ex);
//                    }
//                });
//    }
//
//    @Override
//    @Transactional
//    public Try<GroupDto> unassignUserFromGroup( UUID userId,  UUID groupId){
//        Log.debugf("Unassigning user with id: %s from group with id: %s" , userId, groupId);
//        return Try.of(() -> {
//                    Log.debugf("Checking if group with id: %s exists and retrieve it" , groupId);
//                    GroupEntity group = internalGetEntityById(groupId).getOrElseThrow(ex -> ex);
//
//                    Log.debugf("Checking if user with id: %s is assigned to this group and retrieve it" , userId);
//                    UserEntity userEntity = group.getMembers().stream()
//                            .filter(user -> user.getUserId().equals(userId)).findFirst()
//                            .orElseThrow(() -> new UnknownResourceException("User is not assigned to this group"));
//
//                    group.removeMember(userEntity);
//                    return groupMapper.toDto(group);
//                }).onFailure(ex -> {
//                    switch (ex) {
//                        case UnknownResourceException e -> Log.warn(e.getMessage());
//                        case InvalidInputException e -> Log.warn(e.getMessage());
//                        default -> Log.error("Error unassigning user from group with id: " + groupId, ex);
//                    }
//                });
//    }


}
