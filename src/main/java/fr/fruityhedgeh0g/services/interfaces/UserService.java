package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import io.vavr.control.Try;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface UserService {
    Try<UserDto> getUserById(@NotNull UUID userId);
    Try<List<UserDto>> getAllUsers();
    Try<UserDto> createUser(@NotNull UserDto userDto);
    Try<UserDto> updateUser(@NotNull UserDto userDto);
    Try<Boolean> existsById(@NotNull UUID userId);
    Try<UserEntity> getInternalUserById(@NotNull UUID userId);
    Try<UserDto> assignRoleToUser(@NotNull UUID userId, @NotNull UUID roleId);
    Try<UserDto> unassignRoleFromUser(@NotNull UUID userId, @NotNull UUID roleId);
}
