package fr.fruityhedgeh0g.services;


import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.MandatoryFieldMissingException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.UserRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;


@QuarkusTest
@TestTransaction
class UserServiceTest {
    @InjectMock
    UserRepository userRepository;

    @Inject
    UserServiceImpl userService;

    private UserDto userDto;
    private UserEntity userEntity;

    private UserDto userDtoWithoutId;

    @BeforeEach
    public void setUp() {
        reset(userRepository);

        UUID randomUUID = UUID.randomUUID();
        UUID anotherRandomUUID = UUID.randomUUID();

        userDto = UserDto.builder()
                .userId(randomUUID)
                .firstName("Platy")
                .lastName("Pus")
                .build();

        userEntity = UserEntity.builder()
                .userId(randomUUID)
                .firstName("Platy")
                .lastName("Pus")
                .build();

        userDtoWithoutId = UserDto.builder()
                .firstName("Platy")
                .lastName("Pus")
                .build();


    }

    /** @see UserServiceImpl#existsById(UUID) **/

    @Test
    public void existsById_IdIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> userService.existsById(null));
    }

    @Test
    public void existsById_Success(){
        when(userRepository.existsById(Mockito.any()))
                .thenReturn(true);

        Assertions.assertTrue(userService.existsById(UUID.randomUUID()).get());
    }

    @Test
    public void existsById_UnknownUser_Success() {
        when(userRepository.existsById(Mockito.any()))
                .thenReturn(false);

        Assertions.assertFalse(userService.existsById(UUID.randomUUID()).get());
    }



    /** @see UserServiceImpl#getUserById(UUID) **/

    @Test
    public void getUserById_Success(){
        when(userRepository.findByIdOptional(Mockito.any()))
                .thenReturn(Optional.ofNullable(userEntity));

        Assertions.assertEquals(
                userService.getUserById(UUID.randomUUID()).get(),
                userDto
        );
    }

    /** @see UserServiceImpl#getInternalUserById(UUID) **/

    @Test
    public void getInternalUserById_IdIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> userService.getInternalUserById(null).get());
    }

    @Test
    public void getInternalUserById_UnknownUser_Failure(){
        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> userService.getInternalUserById(UUID.randomUUID()).get());
    }

    @Test
    public void getInternalUserById_Success(){
        when(userRepository.findByIdOptional(Mockito.any()))
                .thenReturn(Optional.ofNullable(userEntity));

        Assertions.assertEquals(
                userService.getInternalUserById(UUID.randomUUID()).get(),
                userEntity
        );

    }

    /** @see UserServiceImpl#updateUser(UserDto) **/

    @Test
    public void updateUser_UnknownUser_Failure() {
        Assertions.assertThrowsExactly(
                UnknownResourceException.class,
                () -> userService.updateUser(userDto).get());

    }

    @Test
    public void updateUser_DtoIsNull_Failure() {
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> userService.updateUser(null).get());
    }

    @Test
    public void updateUser_Success() {
        when(userRepository.findByIdOptional(Mockito.any()))
                .thenReturn(java.util.Optional.of(UserEntity.builder().build()));

        Assertions.assertEquals(userService.updateUser(userDto).get(), userDto);

    }


    /** @see UserServiceImpl#createUser(UserDto) **/

    @Test
    public void createUser_IdMissing_Failure() {
        Assertions.assertThrowsExactly(
                MandatoryFieldMissingException.class,
                () -> userService.createUser(userDtoWithoutId).get()
        );
    }

    @Test
    public void createUser_UserAlreadyExists_Failure() {
        when(userRepository.existsById(Mockito.any())).thenReturn(true);

        Assertions.assertThrowsExactly(
                DuplicateResourceException.class,
                () -> userService.createUser(userDto).get()
        );
    }

    @Test
    public void createUser_DtoIsNull_Failure() {
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> userService.createUser(null).get()
        );
    }

    @Test
    public void createUser_Success() {
        Assertions.assertDoesNotThrow(() -> userService.createUser(userDto).get());

        verify(userRepository).persist(any(UserEntity.class));
    }


}
