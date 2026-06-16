package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Priority(100)
@Authenticated
@Decorator
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class RoleAuthProxy implements RoleService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    RoleService roleService;


    @Override
    public List<RoleDto> listAll() {
        return roleService.listAll();
    }

    @Override
    public Optional<RoleDto> getById(UUID roleId) {
        return roleService.getById(roleId);
    }

    @Override
    public RoleDto create(RoleDto roleDto) {
        return roleService.create(roleDto);
    }

    @Override
    public RoleDto update(RoleDto roleDto) {
        return roleService.update(roleDto);
    }

    @Override
    public void delete(UUID rolId) {
        roleService.delete(rolId);
    }
}
