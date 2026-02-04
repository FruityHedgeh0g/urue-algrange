package fr.fruityhedgeh0g.services;


import fr.fruityhedgeh0g.dtos.UserDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.MandatoryFieldMissingException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.when;


@QuarkusTest
@Transactional
class UserServiceTest {
    @Inject
    UserRepository userRepository;

    @InjectMock
    UserMapper userMapper;

    @InjectMock
    GroupService groupService;

    @Inject
    UserService userService;


    @BeforeEach
    public void setUp() {
        Mockito.reset(userMapper);
        Mockito.reset(groupService);
    }

    @Test
    public void updateUser_UnknownUser_Failure() {
        UserDto userDto = UserDto.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();

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
        UUID randomUUID = UUID.randomUUID();

        UserEntity user = UserEntity.builder()
                .userId(randomUUID)
                .firstName("Platy")
                .lastName("Pus")
                .build();

        userRepository.persist(user);

        UserDto userDto = UserDto.builder()
                .userId(randomUUID)
                .firstName("Hedge")
                .lastName("Hog")
                .build();

        Assertions.assertDoesNotThrow(() -> userService.updateUser(userDto).get());
    }


    @Test
    public void createUser_IdMissing_Failure() {
        UserDto userDto = UserDto.builder()
                .build();

        Assertions.assertThrowsExactly(
                MandatoryFieldMissingException.class,
                () -> userService.createUser(userDto).get()
        );
    }

    @Test
    public void createUser_UserAlreadyExists_Failure() {
        UUID randomUUID = UUID.randomUUID();
        UserEntity user = UserEntity.builder()
                .userId(randomUUID)
                .firstName("Fruity")
                .lastName("Hog")
                .build();

        userRepository.persist(user);

        UserDto userDto = UserDto.builder()
                .userId(randomUUID)
                .build();

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
        UserDto userDto = UserDto.builder()
                .userId(UUID.randomUUID())
                .firstName("Platy")
                .lastName("Pus")
                .build();

        Assertions.assertDoesNotThrow(() -> userService.createUser(userDto).get());

    }


}
