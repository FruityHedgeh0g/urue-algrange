package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import fr.fruityhedgeh0g.enums.RoleTypeEnum;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {

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
