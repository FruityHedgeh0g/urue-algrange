package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Alternative
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
    public Try<List<RoleDto>> listAll() {
        return null;
    }

    @Override
    public Try<RoleDto> getById(UUID roleId) {
        return null;
    }

    @Override
    public Try<RoleDto> create(RoleDto roleDto) {
        return null;
    }

    @Override
    public Try<RoleDto> update(RoleDto roleDto) {
        return null;
    }

    @Override
    public Try<RoleDto> delete(UUID rolId) {
        return null;
    }
}
