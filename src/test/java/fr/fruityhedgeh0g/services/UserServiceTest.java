package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.MandatoryFieldMissingException;
import fr.fruityhedgeh0g.repositories.UserRepository;
import io.quarkus.logging.Log;
import io.quarkus.test.Mock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.UUID;

@QuarkusTest
@Transactional
public class UserServiceTest {

    @Inject
    UserService userService;

    @Test
    public void createUser_IdMissing_Failure() {
        UserDto userDto = UserDto.builder()
                .build();

        Assertions.assertThrowsExactly(
                MandatoryFieldMissingException.class,
                () -> userService.createUser(userDto)
                        .get()
        );
    }

    @Test
    public void createUser_UserAlreadyExists_Failure(){
        UserDto userDto = UserDto.builder()
                .userId(UUID.randomUUID())
                .build();

        //TODO: Debug :D check @InjectMock and @Mock
        //Mockito.when(userRepository.existsById(userDto.getUserId())).thenReturn(true);
        //Mockito.when(userService.existsById(userDto.getUserId()).get()).thenReturn(true);

        Assertions.assertThrowsExactly(
                DuplicateResourceException.class,
                () -> userService.createUser(userDto)
                        .get()
        );
    }

    @Test
    public void createUser_DtoIsNull_Failure(){
        Assertions.assertThrowsExactly(
                ConstraintViolationException.class,
                () -> userService.createUser(null)
                        .get()
        );
    }

    @Test
    public void createUser_Success(){
        UserDto userDto = UserDto.builder()
                .userId(UUID.randomUUID())
                .build();

        Assertions.assertDoesNotThrow(() -> userService.createUser(userDto).get());

    }

}
