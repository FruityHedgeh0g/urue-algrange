package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
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
public class GroupProxy implements GroupService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
    GroupService groupService;

    @Override
    public Try<List<GroupDto>> getAllGroups() {
        return groupService.getAllGroups();
    }

    @Override
    public Try<GroupDto> getGroupById(UUID groupId) {
        return groupService.getGroupById(groupId);
    }

    @Override
    public Try<GroupDto> createGroup(GroupDto groupDto) {
        return groupService.createGroup(groupDto);
    }

    @Override
    public Try<GroupDto> updateGroup(GroupDto groupDto) {
        return groupService.updateGroup(groupDto);
    }

    @Override
    public Try<Void> deleteGroup(UUID groupId) {
        return groupService.deleteGroup(groupId);
    }

    @Override
    public Try<GroupDto> assignUserToGroup(UUID userId, UUID groupId) {
        return groupService.assignUserToGroup(userId, groupId);
    }

    @Override
    public Try<GroupDto> unassignUserFromGroup(UUID userId, UUID groupId) {
        return groupService.unassignUserFromGroup(userId, groupId);
    }

    @Override
    public GroupEntity getInternalEntityById(UUID groupId) {
        return groupService.getInternalEntityById(groupId);
    }
}
