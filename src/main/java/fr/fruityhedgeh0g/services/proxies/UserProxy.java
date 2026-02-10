package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.services.interfaces.UserService;
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
public class UserProxy implements UserService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
    UserService userService;

    @Override
    public Try<UserDto> getUserById(UUID userId) {

        return userService.getUserById(userId);
    }

    @Override
    public Try<List<UserDto>> getAllUsers() {

        return userService.getAllUsers();
    }

    @Override
    public List<UserEntity> internalGetAllUsersFilteredByRole(UUID roleId) {
        return userService.internalGetAllUsersFilteredByRole(roleId);
    }

    @Override
    public Try<UserDto> createUser(UserDto userDto) {

        return userService.createUser(userDto);
    }

    @Override
    public Try<UserDto> updateUser(UserDto userDto) {

        return userService.updateUser(userDto);
    }

    @Override
    public Boolean internalExistsById(UUID userId) {

        return userService.internalExistsById(userId);
    }

    @Override
    public Boolean internalExistsByRole(UUID roleId) {
        return userService.internalExistsByRole(roleId);
    }

    @Override
    public UserEntity internalGetUserById(UUID userId) {

        return userService.internalGetUserById(userId);
    }

    @Override
    public Try<UserDto> assignRoleToUser(UUID userId, UUID roleId) {

        return userService.assignRoleToUser(userId, roleId);
    }

    @Override
    public Try<UserDto> unassignRoleFromUser(UUID userId, UUID roleId) {
        return userService.unassignRoleFromUser(userId, roleId);
    }

}
