package fr.fruityhedgeh0g.dtos.RoleDtos;

import java.util.UUID;

public record NestedRoleDto(UUID roleId, String name, String roleType) {
}
