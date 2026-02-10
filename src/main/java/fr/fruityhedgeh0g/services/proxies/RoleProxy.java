package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import fr.fruityhedgeh0g.enums.RoleTypeEnum;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("proxy")
@Authenticated
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class RoleProxy implements RoleService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
    RoleService roleService;

    @Override
    public Try<List<RoleDto>> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Override
    public Try<List<RoleDto>> getAllRolesFiltered(RoleTypeEnum[] filter) {
        return roleService.getAllRolesFiltered(filter);
    }

    @Override
    public Try<RoleDto> getRoleById(UUID roleId) {
        return roleService.getRoleById(roleId);
    }

    @Override
    public RoleEntity getInternalRoleById(UUID roleId) {
        return null;
    }

    @Override
    public Try<RoleDto> createRole(RoleDto roleDto) {
        return roleService.createRole(roleDto);
    }

    @Override
    public Try<RoleDto> updateRole(RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }

    @Override
    public Try<Void> deleteRole(UUID roleId) {

        return roleService.deleteRole(roleId);
    }
}
