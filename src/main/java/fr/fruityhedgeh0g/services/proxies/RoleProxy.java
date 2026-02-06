package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Authenticated
@Identifier("serviceProxy")
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
        return null;
    }

    @Override
    public Try<RoleDto> getRoleById(UUID roleId) {
        return null;
    }

    @Override
    public Try<RoleDto> createRole(RoleDto roleDto) {
        return null;
    }

    @Override
    public Try<RoleDto> updateRole(RoleDto roleDto) {
        return null;
    }

    @Override
    public void deleteRole(UUID roleId) {

    }
}
