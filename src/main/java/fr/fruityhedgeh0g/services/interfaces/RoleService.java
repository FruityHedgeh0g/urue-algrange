package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import io.vavr.control.Try;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    Try<List<RoleDto>> getAllRoles();
    Try<RoleDto> getRoleById(@NotNull UUID roleId);
    Try<RoleDto> createRole(@NotNull RoleDto roleDto);
    Try<RoleDto> updateRole(@NotNull RoleDto roleDto);
    void deleteRole(@NotNull UUID roleId);


}
