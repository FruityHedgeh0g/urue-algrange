package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
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
public class GroupAuthProxy implements GroupService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    GroupService groupService;


    @Override
    public Try<List<GroupDto>> listAll() {
        return null;
    }

    @Override
    public Try<GroupDto> getById(UUID groupId) {
        return null;
    }

    @Override
    public Try<GroupDto> create(GroupDto groupDto) {
        return null;
    }

    @Override
    public Try<GroupDto> update(GroupDto groupDto) {
        return null;
    }

    @Override
    public Try<GroupDto> delete(UUID groupId) {
        return null;
    }
}
