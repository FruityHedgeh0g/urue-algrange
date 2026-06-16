package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
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
    public List<GroupDto> listAll() {
        return groupService.listAll();
    }

    @Override
    public Optional<GroupDto> getById(UUID groupId) {
        return groupService.getById(groupId);
    }

    @Override
    public GroupDto create(GroupDto groupDto) {
        return groupService.create(groupDto);
    }

    @Override
    public GroupDto update(GroupDto groupDto) {
        return groupService.update(groupDto);
    }

    @Override
    public void delete(UUID groupId) {
        groupService.delete(groupId);
    }
}
