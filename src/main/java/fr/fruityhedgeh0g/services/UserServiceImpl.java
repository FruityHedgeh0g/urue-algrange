package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.MandatoryFieldMissingException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.experimental.PackagePrivate;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    @Transactional
    public Try<UserEntity> getInternalUserById(UUID userId){
        Log.infof("Getting user with id: %s", userId);
        return Try.of(() -> userRepository
                        .findByIdOptional(userId)
                        .orElseThrow(() ->
                                new UnknownResourceException("User not found: " + userId))
                ).onFailure(ex -> {
                    if (ex instanceof UnknownResourceException) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.errorf(ex,"Error getting user with id: %s", userId );
                    }
                });
    }

    @Override
    public Try<UserDto> assignRoleToUser(UUID userId, UUID roleId) {
        return null;
    }

    @Override
    public Try<UserDto> unassignRoleFromUser(UUID userId, UUID roleId) {
        return null;
    }

    @Transactional
    public Try<UserDto> getUserById(UUID userId){
        return getInternalUserById(userId)
                .map(userMapper::toDto)
                .onFailure(e ->
                        Log.errorf(e,"Error getting user with id: %s", userId )
                );
    }

    @Transactional
    public Try<List<UserDto>> getAllUsers(){
        Log.info("Getting all users");
        return Try.of(() -> userRepository
                        .findAll()
                        .stream()
                        .map(userMapper::toDto)
                        .toList())
                .onFailure(e ->
                        Log.error("Error getting all users", e)
                );
    }

    @Transactional
    public Try<UserDto> createUser(UserDto userDto){
        Log.infof("Creating user: %s", userDto);
        return Try.of(() -> {

            Log.debugf("Validating user: %s" , userDto);
            if(userDto.getUserId() == null)
                throw new MandatoryFieldMissingException("User id is mandatory");

            Log.debugf("Searching for already existing user with id: %s" , userDto.getUserId());
            if (userRepository.existsById(userDto.getUserId()))
                throw new DuplicateResourceException("User already exists: " + userDto.getUserId());

            Log.debugf("Persisting new user: " , userDto);
            UserEntity userEntity = userMapper.toEntity(userDto);
            userRepository.persist(userEntity);

            Log.debugf("User created. Sending up-to-date user infos: %s" , userDto);
            return userMapper.toDto(userEntity);

        }).onFailure(ex -> {
            switch (ex){
                case NoSuchElementException e -> Log.warn(e.getMessage());
                case DuplicateResourceException e -> Log.warn(e.getMessage());
                case MandatoryFieldMissingException e -> Log.warn(e.getMessage());
                default -> Log.errorf(ex,"Error creating user: %s" , userDto);
            }
        });
    }

    @Transactional
    @PackagePrivate
    public Try<Boolean> existsById(UUID userId){
        Log.infof("Checking user existence with id: %s", userId);
        return Try.of(() -> userRepository
                        .existsById(userId))
                .onFailure(e ->
                        Log.errorf(e,"Error checking user existence with id: %s" ,userId)
                );
    }

    @Transactional
    public Try<UserDto> updateUser(UserDto userDto){
        Log.infof("Updating user: %s", userDto);
        return Try.of(() -> userRepository
                        .findByIdOptional(userDto.getUserId())
                        .orElseThrow(() ->
                                new UnknownResourceException("User not found: " + userDto.getUserId()))
                ).peek(user ->
                        userMapper.partialDtoToEntity(user, userDto)
                ).map(userMapper::toDto)
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException) {
                        Log.warn(ex.getMessage());
                    }else {
                        Log.errorf(ex,"Error updating user : %s" , userDto);
                    }
                });
    }

//INFO : Non nécéssité de supprimer des users
//    @Transactional
//    public void deleteUser(@NotNull UUID userId){
//        Log.debug("Deleting user with id: " + userId);
//        Try.run(() -> userRepository.deleteById(userId))
//                .onFailure(e -> Log.error("Error deleting user with id: " + userId, e));
//
//    }

}
