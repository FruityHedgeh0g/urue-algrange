package fr.fruityhedgeh0g.dtos.roleDtos;

import java.util.UUID;

public record NestedRoleDto(UUID roleId, String name, String roleType) {
}
