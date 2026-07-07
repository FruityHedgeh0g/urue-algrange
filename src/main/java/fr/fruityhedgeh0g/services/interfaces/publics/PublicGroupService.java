package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicGroupService {
    List<GroupDto> listAll();
    GroupDto getById(@NotNull UUID groupId);
    GroupDto create(@NotNull @Valid GroupDto groupDto );
    GroupDto update(@NotNull @Valid GroupDto groupDto );
    void delete(@NotNull UUID groupId);
    void assignUser(@NotNull UUID groupId, @NotNull UUID userId);
    void unassignUser(@NotNull UUID groupId, @NotNull UUID userId);
}
