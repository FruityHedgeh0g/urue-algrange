package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.utilities.logging.Logged;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalGroupService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
@Logged
@ApplicationScoped
@Default
public class GroupServiceImpl implements GroupService, InternalGroupService {
    @Inject GroupRepository groupRepository;
    @Inject InternalUserService internalUserService;
    @Inject GroupMapper groupMapper;

    @Override
    public List<GroupDto> listAll() {
        return groupRepository.listAll()
                .stream()
                .map(groupMapper::toDto)
                .toList();
    }

    @Override
    public GroupDto getById(UUID groupId) {
        return groupMapper.toDto(
                groupRepository.findByIdOptional(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: "+groupId))
        );
    }

    @Override
    @Transactional
    public GroupDto create(GroupDto groupDto) {
        if (groupRepository.existsByName(groupDto.getName()))
            throw new DuplicateResourceException("This resource already exists in the system.");

        GroupEntity groupEntity = groupMapper.toEntity(groupDto);
        groupRepository.persist(groupEntity);

        return groupMapper.toDto(groupEntity);
    }

    @Override
    @Transactional
    public GroupDto update(GroupDto groupDto) {
        GroupEntity groupEntity = groupRepository.findByIdOptional(groupDto.getGroupId())
                .orElseThrow(() -> new UnknownResourceException("Group not found: "+groupDto.getGroupId()));

        if (!groupEntity.getName().equals(groupDto.getName()) && groupRepository.existsByName(groupDto.getName()))
            throw new DuplicateResourceException("A group with this name already exists in the system.");

        groupEntity = groupMapper.partialDtoToEntity(groupEntity,groupDto);
        groupRepository.persist(groupEntity);

        return groupMapper.toDto(groupEntity);
    }

    @Override
    @Transactional
    public void delete(UUID groupId) {
        GroupEntity groupEntity = groupRepository.findByIdOptional(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: "+groupId));

        if (groupEntity.getSector() != null)
            throw new InvalidResourceException("Group is assigned to a sector, cannot be deleted");

        //todo: développer la suppression.
        groupRepository.deleteById(groupId);
    }

    @Override
    @Transactional
    public GroupDto setChef(UUID groupId, UUID userId) {
        GroupEntity groupEntity = groupOrThrow(groupId);
        UserEntity chef = internalUserService.doGetEntityById(userId)
                .orElseThrow(() -> new UnknownResourceException("User not found: "+userId));

        if (!chef.getRole().canLeadGroupe())
            throw new InvalidResourceException("Only a Chef de groupe or above can lead a Groupe: "+userId);

        groupRepository.findByChef(userId)
                .filter(previous -> !previous.getGroupId().equals(groupId))
                .ifPresent(previous -> {
                    previous.setChef(null);
                    // Frees the unique chef_id before it is given to this Groupe
                    groupRepository.flush();
                });

        groupEntity.setChef(chef);
        return groupMapper.toDto(groupEntity);
    }

    @Override
    @Transactional
    public GroupDto clearChef(UUID groupId) {
        GroupEntity groupEntity = groupOrThrow(groupId);
        groupEntity.setChef(null);
        return groupMapper.toDto(groupEntity);
    }

    @Override
    @Transactional
    public void doEndAffectationOf(UUID userId) {
        groupRepository.findByChef(userId).ifPresent(group -> group.setChef(null));
    }

    @Override
    public Optional<GroupEntity> doGetEntityLedBy(UUID userId) {
        return groupRepository.findByChef(userId);
    }

    @Override
    public List<GroupEntity> doListEntitiesOfSector(UUID sectorId) {
        return groupRepository.list("sector.sectorId = ?1 order by name", sectorId);
    }

    private GroupEntity groupOrThrow(UUID groupId) {
        return groupRepository.findByIdOptional(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: "+groupId));
    }

    @Override
    public Optional<GroupEntity> doGetEntityById(UUID groupId) {
        return groupRepository.findByIdOptional(groupId);
    }


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


}
