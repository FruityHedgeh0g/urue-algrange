package fr.fruityhedgeh0g.services.interfaces.internals;

import fr.fruityhedgeh0g.entities.GroupEntity;

import java.util.Optional;
import java.util.UUID;

public interface InternalGroupService {
    Optional<GroupEntity> getEntityById(UUID groupId);
}
