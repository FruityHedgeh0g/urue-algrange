package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.model.dtos.UserDto;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.experimental.PackagePrivate;

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

    @PackagePrivate
    @Transactional
    public Try<UserEntity> getUserEntityById(@NotNull UUID userId){
        Log.debug("Getting user with id: " + userId);
        return Try.of(() -> userRepository.findByIdOptional(userId)
                        .orElseThrow(() -> new UnknownResourceException("User not found: " + userId)))
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.error("Error getting user with id: " + userId, ex);
                    }
                });
    }

    @Transactional
    public Try<UserDto> getUserById(@NotNull UUID userId){
        return getUserEntityById(userId).map(userMapper::toDto)
                .onFailure(e -> Log.error("A mapping error occurred: " + userId, e));
    }

    @Transactional
    public Try<List<UserDto>> getAllUsers(){
        Log.debug("Getting all users");
        return Try.of(() -> userRepository.findAll()
                        .stream()
                        .map(userMapper::toDto)
                        .toList())
                .onFailure(e -> Log.error("Error getting all users", e));
    }

    public Try<UserDto> createUser(@NotNull UserDto userDto){
        return Try.of(() -> {
            Log.debug("Searching for already existing user with id: " + userDto.getUserId());
            if (userRepository.existsById(userDto.getUserId()))
                throw new DuplicateResourceException("User already exists: " + userDto.getUserId());

            Log.debug("Creating user: " + userDto.getUserId());
            UserEntity userEntity = userMapper.toEntity(userDto);
            userRepository.persist(userEntity);

            Log.debug("User created, retrieving up-to-date user infos: " + userDto.getUserId());
            return userMapper.toDto(userEntity);
        }).onFailure(ex -> {
            switch (ex){
                case NoSuchElementException e -> Log.warn(e.getMessage());
                case DuplicateResourceException e -> Log.warn(e.getMessage());
                default -> Log.error("Error creating user with id: " + userDto.getUserId(), ex);
            }
        });
    }

    public Try<UserDto> updateUser(@NotNull UserDto userDto){
        return Try.of(() -> userRepository.findByIdOptional(userDto.getUserId())
                        .orElseThrow(() -> new UnknownResourceException("User not found: " + userDto.getUserId())))
                .peek(user -> userMapper.updateEntityFromDto(user, userDto))
                .map(userMapper::toDto)
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.error("Error updating user with id: " + userDto.getUserId(), ex);
                    }
                });
    }

//    public void deleteUser(@NotNull UUID userId){
//        Log.debug("Deleting user with id: " + userId);
//        Try.run(() -> userRepository.deleteById(userId))
//                .onFailure(e -> Log.error("Error deleting user with id: " + userId, e));
//
//    }

}
