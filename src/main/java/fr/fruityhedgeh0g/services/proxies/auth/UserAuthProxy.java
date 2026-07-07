package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(100)
@Authenticated
@Decorator
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class UserAuthProxy implements UserService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    UserService userService;


    @Override
    public List<UserDto> listAll() {
        return userService.listAll();
    }

    @Override
    public UserDto getById(UUID userId) {
        return userService.getById(userId);
    }

    @Override
    public UserDto create(UserDto userDto) {
        return userService.create(userDto);
    }

    @Override
    public UserDto update(UserDto userDto) {
        return userService.update(userDto);
    }

    @Override
    public void delete(UUID userId) {
        userService.delete(userId);
    }

    @Override
    public Optional<UserEntity> getEntityById(UUID userId) {
        return userService.getEntityById(userId);
    }
}
