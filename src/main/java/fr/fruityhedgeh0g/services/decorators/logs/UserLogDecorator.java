package fr.fruityhedgeh0g.services.decorators.logs;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(200)
@Decorator
public class UserLogDecorator implements UserService{
    @Inject
    @Delegate
    UserService userService;

    @Override
    public List<UserDto> listAll() {
        Log.debugf("Retrieving all users...");
        return Try.of(userService::listAll)
                .onSuccess(users -> Log.debugf("%d users retrieved.",users.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving users."))
                .get();
    }

    @Override
    public UserDto getById(UUID userId) {
        Log.debugf("Retrieving user by id %s...",userId);
        return Try.of(() -> userService.getById(userId))
                .onSuccess(user -> {
                        Log.debugf("User retrieved: "+user.toString());
                })
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"User with id %s not found.", userId);
                        default -> Log.errorf(t,"An error occurred while retrieving user.");
                    }
                })
                .get();
    }

    @Override
    public UserDto create(UserDto userDto) {
        Log.debugf("Creating new user: %s", userDto.toString());
        return Try.of(() -> userService.create(userDto))
                .onSuccess(user -> Log.debugf("User created."))
                .onFailure(t -> {
                    switch(t){
                        case DuplicateResourceException ex -> Log.errorf(ex,"User %s already existing.", userDto.getUserId());
                        default -> Log.errorf(t,"An error occurred while creating user.");
                    }
                })
                .get();
    }

    @Override
    public UserDto update(UserDto userDto) {
        Log.debugf("Updating an existing user: %s", userDto.toString());
        return Try.of(() -> userService.update(userDto))
                .onSuccess(user -> Log.debugf("User updated."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"User %s not found.", userDto.getUserId());
                        default -> Log.errorf(t,"An error occurred while updating user.");
                    }
                })
                .get();
    }

    @Override
    public void delete(UUID userId) {

        Log.debugf("Deleting user by id %s...",userId);
        Try.run(() -> userService.delete(userId))
                .onSuccess(v -> Log.debugf("User deleted."))
                .onFailure(t -> Log.errorf(t,"An error occurred during user deletion."))
                .get();
    }

    @Override
    public Optional<UserEntity> getEntityById(UUID userId) {
        Log.debugf("[INTERNAL] Retrieve user by id %s...",userId);
        return Try.of(() -> userService.getEntityById(userId))
                .onSuccess(user -> {
                    if (user.isPresent())
                        Log.debugf("User retrieved.");
                    else Log.debugf("User %s not found.",userId);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving user."))
                .get();
    }
}
