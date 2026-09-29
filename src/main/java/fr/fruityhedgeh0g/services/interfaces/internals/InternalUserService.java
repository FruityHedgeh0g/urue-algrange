package fr.fruityhedgeh0g.services.interfaces.internals;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface InternalUserService {
    UserDto doCreate(@NotNull @Valid UserDto userDto);
    UserDto doUpdate(@NotNull @Valid UserDto userDto);
    void doDelete(@NotNull UUID userId);
    Optional<UserEntity> doGetEntityById(@NotNull UUID userId);
}
