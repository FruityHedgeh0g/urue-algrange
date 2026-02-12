package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface UserService {
    Try<UserDto> getUserById(@NotNull UUID userId);
    Try<List<UserDto>> getAllUsers();
    Try<List<UserEntity>> internalGetAllUsersFilteredByRole(@NotNull UUID roleId);
    Try<UserDto> createUser(@NotNull @Valid UserDto userDto);
    Try<UserDto> updateUser(@NotNull @Valid UserDto userDto);
    Try<Boolean> internalExistsById(@NotNull UUID userId);
    Try<Boolean> internalExistsByRole(@NotNull UUID roleId);
    Try<UserEntity> internalGetUserById(@NotNull UUID userId);
    Try<UserDto> assignRoleToUser(@NotNull UUID userId, @NotNull UUID roleId);
    Try<UserDto> unassignRoleFromUser(@NotNull UUID userId, @NotNull UUID roleId);
}
