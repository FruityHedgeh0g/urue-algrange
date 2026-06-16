package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GroupService {

    Try<List<GroupDto>> listAll();
    Try<GroupDto> getById(@NotNull UUID groupId);
    Try<GroupDto> create(@NotNull @Valid GroupDto groupDto );
    Try<GroupDto> update(@NotNull @Valid GroupDto groupDto );
    Try<GroupDto> delete(@NotNull UUID groupId);

//    Try<List<GroupDto>> getAllGroups();
//    Try<GroupDto> getGroupById(@NotNull UUID groupId);
//    Try<GroupDto> createGroup(@NotNull @Valid GroupDto groupDto);
//    Try<GroupDto> updateGroup(@NotNull @Valid GroupDto groupDto);
//    Try<Void> deleteGroup(@NotNull UUID groupId);
//    Try<GroupDto> assignUserToGroup(@NotNull UUID userId, @NotNull UUID groupId);
//    Try<GroupDto> unassignUserFromGroup(@NotNull UUID userId, @NotNull UUID groupId);
//    Try<GroupEntity> internalGetEntityById(@NotNull UUID groupId);
//    Try<Boolean> internalExistsById(@NotNull UUID groupId);
//    Try<Set<GroupEntity>> internalGetBySectorId(@NotNull UUID sectorId);
//    Try<Set<GroupDto>> getGroupsBySectorId(@NotNull UUID sectorId);
}
