package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.enums.RoleEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicUserService {
    List<UserDto> listAll();
    UserDto getById(@NotNull UUID userId);

    /** Promotion chain: see {@link fr.fruityhedgeh0g.enums.RoleEnum#maySetRole}. The database Role applies even if the Keycloak mirror fails. */
    UserDto changeRole(@NotNull UUID actorId, @NotNull UUID personId, @NotNull RoleEnum role);



//    Try<UserDto> getUserById(@NotNull UUID userId);
//    Try<List<UserDto>> getAllUsers();
//    Try<UserDto> createUser(@NotNull @Valid UserDto userDto);
//    Try<UserDto> updateUser(@NotNull @Valid UserDto userDto);
//    Try<Boolean> internalExistsById(@NotNull UUID userId);
//    Try<UserEntity> internalGetUserById(@NotNull UUID userId);
}
