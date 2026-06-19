package fr.fruityhedgeh0g.services.interfaces.internals;

import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface InternalUserService {
    Optional<UserEntity> getEntityById(@NotNull UUID userId);
}
