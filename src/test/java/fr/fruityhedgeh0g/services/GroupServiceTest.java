package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@QuarkusTest
public class GroupServiceTest {
    @Inject
    GroupServiceImpl groupService;

    @Inject
    GroupRepository groupRepository;

    @Inject
    GroupMapper groupMapper;

    @Inject
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {

    }

    /** @see GroupServiceImpl#listAll() () **/

    @Test
    @TestTransaction
    public void listAllGroups_Success(){
        GroupEntity firstGroup = GroupEntity.builder().name("First group").build();
        groupRepository.persist(firstGroup);

        GroupEntity secondGroup = GroupEntity.builder().name("Second group").build();
        groupRepository.persist(secondGroup);

        List<GroupEntity> comparativeGroups = List.of(firstGroup,secondGroup);
        List<GroupEntity> gatheredGroups = groupService.listAll().stream().map(groupMapper::toEntity).toList();

        Assertions.assertEquals(comparativeGroups.size(), gatheredGroups.size());
        Assertions.assertIterableEquals(comparativeGroups, gatheredGroups);
    }

    @Test
    @TestTransaction
    public void getAllGroups_NotFound(){
        List<GroupDto> groups = new ArrayList<>();
        List<GroupDto> gatheredGroups = groupService.listAll();
        Assertions.assertEquals(0,gatheredGroups.size());
        Assertions.assertEquals(groups, gatheredGroups);
    }

    /** @see GroupServiceImpl#getById(UUID) () **/

    //Ne marche pas car nécéssite un override de equals et hashCode
    @Test
    @TestTransaction
    public void getById_Success(){
        GroupEntity firstGroup = GroupEntity.builder().name("Test group").build();
        groupRepository.persist(firstGroup);

        GroupEntity retrievedGroup = groupMapper.toEntity(groupService.getById(firstGroup.getGroupId()));
        Assertions.assertEquals(firstGroup, retrievedGroup);

    }

    @Test
    @TestTransaction
    public void getById_NotFound(){
        Assertions.assertThrows(UnknownResourceException.class, () -> groupService.getById(UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void getById_NullId(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> groupService.getById(null));
    }

    /** @see GroupServiceImpl#create(GroupDto) () **/

    @Test
    @TestTransaction
    public void create_NullDto(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> groupService.create(null));
    }

    @Test
    @TestTransaction
    public void create_Success(){
        GroupDto groupDto = groupService.create(
                GroupDto.builder().name("Test Group").build()
        );

        GroupDto retrievedGroup = groupService.getById(groupDto.getGroupId());
        Assertions.assertEquals(groupDto,retrievedGroup);
    }

    @Test
    @TestTransaction
    public void create_Duplicate(){
        GroupEntity group = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(group);

        Assertions.assertTrue(groupRepository.existsByName(group.getName()));

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> groupService.create(groupMapper.toDto(group))
        );

    }

    /** @see GroupServiceImpl#update(GroupDto) () **/

    @Test
    @TestTransaction
    public void update_Success(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        Assertions.assertTrue(groupRepository.existsByName(groupEntity.getName()));

        GroupDto updatedGroupDto = GroupDto.builder()
                .name("Updated Group")
                .groupId(groupEntity.getGroupId())
                .build();

        Assertions.assertDoesNotThrow(() -> groupService.update(updatedGroupDto));

        Assertions.assertTrue(groupRepository.existsByName(groupEntity.getName()));

    }

    @Test
    @TestTransaction
    public void update_NotFound(){
        GroupDto groupDto = GroupDto
                .builder()
                .name("Test Group")
                .groupId(UUID.randomUUID())
                .build();

        Assertions.assertThrows(UnknownResourceException.class, () -> groupService.update(groupDto));

    }

    @Test
    @TestTransaction
    public void update_NullDto(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> groupService.update(null));
    }

    @Test
    @TestTransaction
    public void update_Duplicate(){
        groupRepository.persist(GroupEntity.builder().name("Test Group").build());

        GroupEntity groupEntity = GroupEntity.builder().name("To be renamed Group").build();
        groupRepository.persist(groupEntity);

        Assertions.assertTrue(groupRepository.existsByName(groupEntity.getName()));

        GroupDto updatedGroupDto = GroupDto.builder()
                .name("Test Group")
                .groupId(groupEntity.getGroupId())
                .build();

        Assertions.assertThrows(
                DuplicateResourceException.class,
                () -> groupService.update(updatedGroupDto)
        );
    }

    /** @see GroupServiceImpl#delete(UUID) () **/

    @Test
    @TestTransaction
    public void delete_NullId(){
        Assertions.assertThrows(ConstraintViolationException.class, () -> groupService.delete(null));
    }

    @Test
    @TestTransaction
    public void delete_NotFound(){
        Assertions.assertThrows(UnknownResourceException.class, () -> groupService.delete(UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void delete_UserAssigned(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();

        groupRepository.persist(groupEntity);

        groupEntity.addMember(userEntity);

        Assertions.assertThrows(InvalidResourceException.class, () -> groupService.delete(groupEntity.getGroupId()));
    }

    @Test
    @TestTransaction
    public void delete_Success(){

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        Assertions.assertDoesNotThrow(() -> groupService.delete(groupEntity.getGroupId()));
        Assertions.assertThrows(UnknownResourceException.class, () -> groupService.getById(groupEntity.getGroupId()));
    }

    /** @see GroupServiceImpl#assignUser(UUID,UUID) () **/

    @Test
    @TestTransaction
    public void assignUser_NullGroupId(){
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> groupService.assignUser(null, UUID.randomUUID())
        );
    }

    @Test
    @TestTransaction
    public void assignUser_NullUserId(){
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> groupService.assignUser(UUID.randomUUID(), null)
        );
    }

    @Test
    @TestTransaction
    public void assignUser_UserNotFound(){
        Assertions.assertThrows(
                UnknownResourceException.class,
                () -> groupService.assignUser(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void assignUser_GroupNotFound(){
        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();
        userRepository.persist(userEntity);

        Assertions.assertThrows(UnknownResourceException.class,
                () -> groupService.assignUser(UUID.randomUUID() , userEntity.getUserId()));
    }

    @Test
    @TestTransaction
    public void assignUser_UserAlreadyAssigned(){
        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();
        userRepository.persist(userEntity);

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        GroupEntity anotherGroupEntity = GroupEntity.builder().name("Another Group").build();
        groupRepository.persist(anotherGroupEntity);

        anotherGroupEntity.addMember(userEntity);

        Assertions.assertThrows(DuplicateResourceException.class,
                () -> groupService.assignUser(groupEntity.getGroupId(), userEntity.getUserId()));
    }

    @Test
    @TestTransaction
    public void assignUser_Success(){
        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();
        userRepository.persist(userEntity);

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        Assertions.assertDoesNotThrow(() -> groupService.assignUser(groupEntity.getGroupId(), userEntity.getUserId()));
    }

    @Test
    @TestTransaction
    public void assignUser_AlreadyPerformed(){
        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();
        userRepository.persist(userEntity);

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        groupEntity.addMember(userEntity);

        Assertions.assertDoesNotThrow(() -> groupService.assignUser(groupEntity.getGroupId(), userEntity.getUserId()));
    }

    /** @see GroupServiceImpl#unassignUser(UUID,UUID) () **/

    @Test
    @TestTransaction
    public void unassignUser_Success(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();
        userRepository.persist(userEntity);

        groupEntity.addMember(userEntity);

        Assertions.assertDoesNotThrow(() -> groupService.unassignUser(groupEntity.getGroupId(), userEntity.getUserId()));
    }

    @Test
    @TestTransaction
    public void unassignUser_AlreadyPerformed(){
        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();
        userRepository.persist(userEntity);

        Assertions.assertDoesNotThrow(() -> groupService.unassignUser(groupEntity.getGroupId(), userEntity.getUserId()));
    }

    @Test
    @TestTransaction
    public void unassignUser_UserNotFound(){
        Assertions.assertThrows(
                UnknownResourceException.class,
                () -> groupService.unassignUser(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    @TestTransaction
    public void unassignUser_GroupNotFound(){

        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();
        userRepository.persist(userEntity);

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        groupEntity.addMember(userEntity);

        Assertions.assertThrows(UnknownResourceException.class,
                () -> groupService.unassignUser(UUID.randomUUID(), userEntity.getUserId()));
    }

    @Test
    @TestTransaction
    public void unassignUser_NullGroupId(){
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> groupService.unassignUser(null, UUID.randomUUID())
        );
    }

    @Test
    @TestTransaction
    public void unassignUser_NullUserId(){
        Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> groupService.unassignUser(UUID.randomUUID(),null)
        );
    }

    @Test
    @TestTransaction
    public void unassignUser_UserAssignedToAnotherGroup(){
        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();
        userRepository.persist(userEntity);

        GroupEntity groupEntity = GroupEntity.builder().name("Test Group").build();
        groupRepository.persist(groupEntity);

        GroupEntity anotherGroupEntity = GroupEntity.builder().name("Another Group").build();
        groupRepository.persist(anotherGroupEntity);

        anotherGroupEntity.addMember(userEntity);

        Assertions.assertThrows(InvalidResourceException.class,
                () -> groupService.unassignUser(groupEntity.getGroupId(), userEntity.getUserId()));
    }

}
