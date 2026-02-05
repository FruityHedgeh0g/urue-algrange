package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Identifier("userProxy")
@AllArgsConstructor
@ApplicationScoped
public class UserProxy implements UserService {
    @Inject
    UserService userService;

    @Override
    public Try<UserDto> getUserById(UUID userId) {
        return null;
    }

    @Override
    public Try<List<UserDto>> getAllUsers() {
        return null;
    }

    @Override
    public Try<UserDto> createUser(UserDto userDto) {
        return null;
    }

    @Override
    public Try<UserDto> updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public Try<Boolean> existsById(UUID userId) {
        return null;
    }

    @Override
    public Try<UserEntity> getInternalUserById(UUID userId) {
        return null;
    }
}
