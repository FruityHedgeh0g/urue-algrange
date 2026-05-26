package fr.fruityhedgeh0g.services;


import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.entities.roles.LegalRoleEntity;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;


@QuarkusTest
@TestTransaction
class UserServiceTest {
    @InjectMock
    UserRepository userRepository;

    @InjectMock
    RoleService roleService;

    @Inject
    UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        reset(userRepository);
        reset(roleService);
    }

    /** @see UserServiceImpl#internalGetUserById(UUID)   **/

    @Test
    public void InternalGetUserById_Success(){
        UserEntity userEntity = UserEntity.builder().userId(UUID.randomUUID())
                .firstName("Platy").lastName("Pus").build();

        when(userRepository.findByIdOptional(userEntity.getUserId())).thenReturn(Optional.of(userEntity));
        Assertions.assertEquals(
                userService.internalGetUserById(userEntity.getUserId()).get(),
                userEntity
        );
    }

    @Test
    public void InternalGetUserById_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.internalGetUserById(null)
        );
    }

    @Test
    public void InternalGetUserById_Failure_NotManagedException(){
        UserEntity userEntity = UserEntity.builder().userId(UUID.randomUUID())
                .firstName("Platy").lastName("Pus").build();

        when(userRepository.findByIdOptional(userEntity.getUserId())).thenThrow(new RuntimeException("Dummy exception"));
        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> userService.internalGetUserById(userEntity.getUserId()).get()
        );
    }

    /** @see UserServiceImpl#getUserById(UUID)  **/

    @Test
    public void GetUserById_Success(){
        UserEntity userEntity = UserEntity.builder().userId(UUID.randomUUID())
                .firstName("Platy").lastName("Pus").build();

        UserDto userDto = UserDto.builder().userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName()).lastName(userEntity.getLastName())
                .build();

        when(userRepository.findByIdOptional(userEntity.getUserId())).thenReturn(Optional.of(userEntity));
        Assertions.assertEquals(
                userService.getUserById(userEntity.getUserId()).get(),
                userDto
        );
    }

    @Test
    public void GetUserById_Failure_UnknownResource(){
        UserEntity userEntity = UserEntity.builder().userId(UUID.randomUUID()).build();

        Assertions.assertThrowsExactly(UnknownResourceException.class,
                () -> userService.getUserById(userEntity.getUserId()).get()
        );
    }

    @Test
    public void GetUserById_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.getUserById(null)
        );
    }

    @Test
    public void GetUserById_Failure_NotManagedException(){
        UserEntity userEntity = UserEntity.builder().userId(UUID.randomUUID()).build();

        when(userRepository.findByIdOptional(userEntity.getUserId())).thenThrow(new RuntimeException("Dummy exception"));
        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> userService.getUserById(userEntity.getUserId()).get()
        );
    }

    /** @see UserServiceImpl#getAllUsers()  **/

    @Test
    public void GetAllUsers_Success(){
        UserEntity userEntity = UserEntity.builder().userId(UUID.randomUUID())
                .firstName("Platy").lastName("Pus").build();

        UserDto userDto = UserDto.builder().userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName()).lastName(userEntity.getLastName())
                .build();

        UserEntity anotherUserEntity = UserEntity.builder().userId(UUID.randomUUID())
                .firstName("Hedge").lastName("Hog").build();

        UserDto anotherUserDto = UserDto.builder().userId(anotherUserEntity.getUserId())
                .firstName(anotherUserEntity.getFirstName()).lastName(anotherUserEntity.getLastName())
                .build();

        List<UserEntity> userEntities = List.of(userEntity, anotherUserEntity);
        List<UserDto> userDtos = List.of(userDto, anotherUserDto);

        PanacheQuery<UserEntity> mockedPanacheQuery = mock(PanacheQuery.class);
        when(mockedPanacheQuery.page(any())).thenReturn(mockedPanacheQuery);
        when(mockedPanacheQuery.stream()).thenReturn(userEntities.stream());
        when(userRepository.findAll()).thenReturn(mockedPanacheQuery);

        Assertions.assertEquals(userService.getAllUsers().get(),
                userDtos
        );
    }

    @Test
    public void GetAllUsers_Failure_NotManagedException(){
        when(userRepository.findAll()).thenThrow(new RuntimeException("Dummy exception"));
        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> userService.getAllUsers().get()
        );
    }

    /** @see UserServiceImpl#createUser(UserDto) **/
    @Test
    public void CreateUser_Success(){
        UserDto userDto = UserDto.builder().userId(UUID.randomUUID())
                .firstName("Octo").lastName("Gon").build();

        Assertions.assertEquals(userService.createUser(userDto).get(),
                userDto
        );
    }

    @Test
    public void CreateUser_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.createUser(null)
        );
    }

    @Test
    public void CreateUser_Failure_ConstraintViolation_UserIdIsNull(){
        UserDto userDto = UserDto.builder().firstName("Octo").lastName("Gon").build();
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.createUser(userDto).get()
        );

    }

    @Test
    public void CreateUser_Failure_DuplicateResource(){

    }

    @Test
    public void CreateUser_Failure_NotManagedException(){

    }


    /** @see UserServiceImpl#updateUser(UserDto) **/

    @Test
    public void UpdateUser_Success(){

    }

    @Test
    public void UpdateUser_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.updateUser(null)
        );

    }

    @Test
    public void UpdateUser_Failure_UnknownResource(){

    }

    @Test
    public void UpdateUser_Failure_NotManagedException(){

    }

    /** @see UserServiceImpl#internalGetAllUsersFilteredByRole(UUID) **/

    @Test
    public void InternalGetAllUsersFilteredByRole_Success(){
        RoleEntity roleEntity = LegalRoleEntity.builder()
                .roleId(UUID.randomUUID())
                .name("Black_Templar")
                .build();

        UserEntity userEntity = UserEntity.builder().userId(UUID.randomUUID())
                .firstName("Platy").lastName("Pus").build();

        UserEntity anotherUserEntity = UserEntity.builder().userId(UUID.randomUUID())
                .firstName("Hedge").lastName("Hog").build();

        List<UserEntity> userEntities = List.of(userEntity, anotherUserEntity);

        when(userRepository.findByRole(roleEntity.getRoleId())).thenReturn(userEntities);

        Assertions.assertEquals(
                userService.internalGetAllUsersFilteredByRole(roleEntity.getRoleId()).get(),
                userEntities
        );

    }

    @Test
    public void InternalGetAllUsersFilteredByRole_Failure_NotManagedException(){

    }

    @Test
    public void InternalGetAllUsersFilteredByRole_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.internalGetAllUsersFilteredByRole(null)
        );

    }

    /** @see UserServiceImpl#internalExistsById **/

    @Test
    public void InternalExistsById_Success_True(){

    }

    @Test
    public void InternalExistsById_Success_False(){

    }



    @Test
    public void InternalExistsById_Failure_NotManagedException(){

    }

    @Test
    public void InternalExistsById_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.internalExistsById(null)
        );

    }

    /** @see UserServiceImpl#internalExistsByRole **/

    @Test
    public void InternalExistsByRole_Success_True(){

    }

    @Test
    public void InternalExistsByRole_Success_False(){

    }


    @Test
    public void InternalExistsByRole_Failure_NotManagedException(){

    }

    @Test
    public void InternalExistsByRole_Failure_ConstraintViolation(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.internalExistsByRole(null)
        );

    }

    /** @see UserServiceImpl#assignRoleToUser(UUID, UUID) **/

    @Test
    public void AssignRoleToUser_Success(){

    }

    @Test
    public void AssignRoleToUser_Failure_ConstraintViolation_UserIdIsNull(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.assignRoleToUser(null, UUID.randomUUID()).get()
        );

    }

    @Test
    public void AssignRoleToUser_Failure_ConstraintViolation_RoleIdIsNull(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.assignRoleToUser(UUID.randomUUID(),null).get()
        );

    }

    @Test
    public void AssignRoleToUser_Failure_UnknownResource_UserId(){

    }

    @Test
    public void AssignRoleToUser_Failure_UnknownResource_RoleId(){

    }

    @Test
    public void AssignRoleToUser_Failure_NotManagedException(){

    }

    @Test
    public void AssignRoleToUser_Failure_DuplicateResource(){

    }

    /** @see UserServiceImpl#unassignRoleFromUser(UUID, UUID) **/

    @Test
    public void UnassignRoleFromUser_Success(){

    }

    @Test
    public void UnassignRoleFromUser_Failure_ConstraintViolation_UserIdIsNull(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.unassignRoleFromUser(null, UUID.randomUUID()).get()
        );

    }

    @Test
    public void UnassignRoleFromUser_Failure_ConstraintViolation_RoleIdIsNull(){
        Assertions.assertThrowsExactly(ConstraintViolationException.class,
                () -> userService.unassignRoleFromUser(UUID.randomUUID(),null).get()
        );
    }

    @Test
    public void UnassignRoleFromUser_Failure_UnknownResource_UserId(){

    }

    @Test
    public void UnassignRoleFromUser_Failure_UnknownResource_RoleNotFoundInUser(){

    }

    @Test
    public void UnassignRoleFromUser_Failure_NotManagedException(){

    }











}
