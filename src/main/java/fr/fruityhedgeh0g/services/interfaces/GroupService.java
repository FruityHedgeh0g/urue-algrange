package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import io.vavr.control.Try;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface GroupService {
    Try<List<GroupDto>> getAllGroups();
    Try<GroupDto> getGroupById(@NotNull UUID groupId);
    Try<GroupDto> createGroup(@NotNull GroupDto groupDto);
    Try<GroupDto> updateGroup(@NotNull GroupDto groupDto);
    void deleteGroup(@NotNull UUID groupId);
    Try<GroupDto> assignUserToGroup(@NotNull UUID userId, @NotNull UUID groupId);
    Try<GroupDto> unassignUserFromGroup(@NotNull UUID userId, @NotNull UUID groupId);
    Try<GroupEntity> getInternalEntityById(@NotNull UUID groupId);
}
