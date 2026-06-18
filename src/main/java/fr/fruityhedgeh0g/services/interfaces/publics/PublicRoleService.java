package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicRoleService {

    List<RoleDto> listAll();
    Optional<RoleDto> getById(@NotNull UUID roleId);
    RoleDto create(@NotNull @Valid RoleDto roleDto);
    RoleDto update(@NotNull @Valid RoleDto roleDto);
    void delete(@NotNull UUID roleId);

//    Try<List<RoleDto>> getAllRoles();
//    Try<List<RoleDto>> getAllRolesFilteredByRoleType(@NotNull @Size(min = 1) RoleTypeEnum[] filter);
//    Try<RoleDto> getRoleById(@NotNull UUID roleId);
//    Try<RoleEntity> internalGetRoleById(@NotNull UUID roleId);
//    Try<RoleDto> createRole(@NotNull @Valid RoleDto roleDto);
//    Try<RoleDto> updateRole(@NotNull @Valid RoleDto roleDto);
//    Try<Void> deleteRole(@NotNull UUID roleId);


}
