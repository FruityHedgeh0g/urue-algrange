package fr.fruityhedgeh0g.services.interfaces.internals;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface InternalGroupService {
    Optional<GroupEntity> doGetEntityById(@NotNull UUID groupId);
    Optional<GroupEntity> doGetEntityByUserId(@NotNull UUID userId);
}
