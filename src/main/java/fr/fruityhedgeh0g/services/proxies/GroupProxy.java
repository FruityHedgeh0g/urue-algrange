package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@Identifier("groupProxy")
@AllArgsConstructor
@ApplicationScoped
@Authenticated
public class GroupProxy implements GroupService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    GroupService groupService;

    @Override
    public Try<List<GroupDto>> getAllGroups() {
        return null;
    }

    @Override
    public Try<GroupDto> getGroupById(UUID groupId) {
        return null;
    }

    @Override
    public Try<GroupDto> createGroup(GroupDto groupDto) {
        return null;
    }

    @Override
    public Try<GroupDto> updateGroup(GroupDto groupDto) {
        return null;
    }

    @Override
    public void deleteGroup(UUID groupId) {

    }

    @Override
    public Try<GroupDto> assignUserToGroup(UUID userId, UUID groupId) {
        return null;
    }

    @Override
    public Try<GroupDto> unassignUserFromGroup(UUID userId, UUID groupId) {
        return null;
    }

    @Override
    public Try<GroupEntity> getInternalEntityById(UUID groupId) {
        return null;
    }
}
