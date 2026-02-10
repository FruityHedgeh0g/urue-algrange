package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface GroupService {
    Try<List<GroupDto>> getAllGroups();
    Try<GroupDto> getGroupById(@NotNull UUID groupId);
    Try<GroupDto> createGroup(@NotNull @Valid GroupDto groupDto);
    Try<GroupDto> updateGroup(@NotNull @Valid GroupDto groupDto);
    Try<Void> deleteGroup(@NotNull UUID groupId);
    Try<GroupDto> assignUserToGroup(@NotNull UUID userId, @NotNull UUID groupId);
    Try<GroupDto> unassignUserFromGroup(@NotNull UUID userId, @NotNull UUID groupId);
    GroupEntity internalGetEntityById(@NotNull UUID groupId) throws UnknownResourceException;
}
