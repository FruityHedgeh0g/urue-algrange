package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicUserService {
    List<UserDto> listAll();
    Optional<UserDto> getById(@NotNull UUID userId);
    UserDto create(@NotNull @Valid UserDto userDto);
    UserDto update(@NotNull @Valid UserDto userDto);
    void delete(@NotNull UUID userId);


//    Try<UserDto> getUserById(@NotNull UUID userId);
//    Try<List<UserDto>> getAllUsers();
//    Try<List<UserEntity>> internalGetAllUsersFilteredByRole(@NotNull UUID roleId);
//    Try<UserDto> createUser(@NotNull @Valid UserDto userDto);
//    Try<UserDto> updateUser(@NotNull @Valid UserDto userDto);
//    Try<Boolean> internalExistsById(@NotNull UUID userId);
//    Try<Boolean> internalExistsByRole(@NotNull UUID roleId);
//    Try<UserEntity> internalGetUserById(@NotNull UUID userId);
//    Try<UserDto> assignRoleToUser(@NotNull UUID userId, @NotNull UUID roleId);
//    Try<UserDto> unassignRoleFromUser(@NotNull UUID userId, @NotNull UUID roleId);
}
