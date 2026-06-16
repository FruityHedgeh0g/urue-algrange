package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(200)
@Decorator
public class UserLogProxy implements UserService{
    @Inject
    @Delegate
    UserService userService;

    @Override
    public List<UserDto> listAll() {
        Log.debugf("Trying to retrieve all users.");
        return Try.of(userService::listAll)
                .onSuccess(users -> Log.debugf("%d users retrieved.",users.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving users."))
                .get();
    }

    @Override
    public Optional<UserDto> getById(UUID userId) {
        Log.debugf("Trying to retrieve user by id %s.",userId);
        return Try.of(() -> userService.getById(userId))
                .onSuccess(user -> {
                    if (user.isPresent())
                        Log.debugf("User retrieved.");
                    else Log.debugf("There is no user with id %s.",userId);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving user."))
                .get();
    }

    @Override
    public UserDto create(UserDto userDto) {
        Log.debugf("Trying to create a new user : %s", userDto.toString());
        return Try.of(() -> userService.create(userDto))
                .onSuccess(user -> Log.debugf("User created."))
                .onFailure(t -> {
                    switch(t){
                        case DuplicateResourceException ex -> Log.errorf(ex,"A user already exists for the id provided [%s].", userDto.getUserId());
                        default -> Log.errorf(t,"An error occurred while creating user.");
                    }
                })
                .get();
    }

    @Override
    public UserDto update(UserDto userDto) {
        Log.debugf("Trying to update an existing user : %s", userDto.toString());
        return Try.of(() -> userService.update(userDto))
                .onSuccess(user -> Log.debugf("User updated."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"There is no user with id %s.", userDto.getUserId());
                        default -> Log.errorf(t,"An error occurred while updating user.");
                    }
                })
                .get();
    }

    @Override
    public void delete(UUID userId) {

        Log.debugf("Trying to delete user by id %s.",userId);
        Try.run(() -> userService.delete(userId))
                .onSuccess(v -> Log.debugf("User deleted."))
                .onFailure(t -> Log.errorf(t,"An error occurred during user deletion."))
                .get();
    }
}
