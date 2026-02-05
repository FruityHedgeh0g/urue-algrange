package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import io.quarkus.logging.Log;
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

@Identifier("userProxy")
@AllArgsConstructor
@ApplicationScoped
@Authenticated
public class UserProxy implements UserService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
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
    public Try<UserDto> createUser(UserDto userDto) {
        return userService.createUser(userDto);
    }

    @Override
    public Try<UserDto> updateUser(UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @Override
    public Try<Boolean> existsById(UUID userId) {
        return userService.existsById(userId);
    }

    @Override
    public Try<UserEntity> getInternalUserById(UUID userId) {
        return userService.getInternalUserById(userId);
    }
}
