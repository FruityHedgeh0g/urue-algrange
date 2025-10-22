package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.dtos.UserDto;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.logging.Log;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import io.vavr.control.Try;
import io.vertx.ext.auth.impl.jose.JWT;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.keycloak.representations.JsonWebToken;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class UserService {


    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    public Try<UserDto> getUserById(@NotNull UUID userId){
        Log.debug("Getting user with id: " + userId);
        return Try.of(() -> userRepository
                        .findByIdOptional(userId)
                        .orElseThrow(NoSuchElementException::new))
                .map(userMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("User not found: " + userId);
                    }else {
                        Log.error("Error getting user with id: " + userId, e);
                    }
                });
    }

    public Try<UserDto> createUser(@NotNull UserDto userDto){
        Log.debug("Creating user: " + userDto.getUserId());
        return null;
    }

    public Try<UserDto> updateUser(@NotNull UserDto userDto){
        Log.debug("Updating user: " + userDto.getUserId());
        return null;
    }

    public void deleteUser(@NotNull UUID userId){
        Log.debug("Deleting user with id: " + userId);
        Try.run(() -> userRepository.deleteById(userId))
                .onFailure(e -> Log.error("Error deleting user with id: " + userId, e));

    }

    public Try<List<UserDto>> getAllUsers(){
        Log.debug("Getting all users");
        return Try.of(() -> userRepository
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all users", e));
    }

}
