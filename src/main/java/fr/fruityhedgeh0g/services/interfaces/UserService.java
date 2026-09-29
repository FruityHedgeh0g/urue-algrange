package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicUserService;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends PublicUserService, InternalUserService {


//    Try<UserDto> getUserById(@NotNull UUID userId);
//    Try<List<UserDto>> getAllUsers();
//    Try<UserDto> createUser(@NotNull @Valid UserDto userDto);
//    Try<UserDto> updateUser(@NotNull @Valid UserDto userDto);
//    Try<Boolean> internalExistsById(@NotNull UUID userId);
//    Try<UserEntity> internalGetUserById(@NotNull UUID userId);
}
