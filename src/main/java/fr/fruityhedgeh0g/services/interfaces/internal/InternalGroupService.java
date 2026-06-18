package fr.fruityhedgeh0g.services.interfaces.internal;

import fr.fruityhedgeh0g.entities.GroupEntity;
import io.smallrye.common.constraint.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface InternalGroupService {

    Optional<GroupEntity> getInternalEntityById(@NotNull UUID groupId);
}
