package fr.fruityhedgeh0g.services;


import fr.fruityhedgeh0g.dtos.UserDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.MandatoryFieldMissingException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;


@QuarkusTest
@TestTransaction
class UserServiceTest {
    @InjectMock
    UserRepository userRepository;

    @Inject
    UserService userService;

    private UserDto userDto;
    private UserDto userDtoWithoutId;
    private UserEntity userEntity;



    @BeforeEach
    public void setUp() {
        reset(userRepository);

        UUID randomUUID = UUID.randomUUID();

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

    @Test
    public void getUserById_Success(){
        when(userRepository.findByIdOptional(Mockito.any()))
                .thenReturn(Optional.ofNullable(userEntity));

        Assertions.assertEquals(
                userService.getUserById(UUID.randomUUID()).get(),
                userDto
        );
    }

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
