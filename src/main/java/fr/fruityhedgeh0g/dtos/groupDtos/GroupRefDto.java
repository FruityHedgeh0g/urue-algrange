package fr.fruityhedgeh0g.dtos.groupDtos;

import fr.fruityhedgeh0g.entities.GroupEntity;

import java.util.UUID;

public record GroupRefDto(UUID groupId, String name) {
    public static GroupRefDto of(GroupEntity group) {
        return group == null ? null : new GroupRefDto(group.getGroupId(), group.getName());
    }
}
