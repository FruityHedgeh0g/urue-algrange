package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.MandatoryFieldMissingException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
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

import java.util.*;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    @Identifier("serviceImpl")
    RoleService roleService;

    @Inject
    UserMapper userMapper;

    @Override
    public UserEntity internalGetUserById(UUID userId) throws UnknownResourceException{
        Log.infof("Getting user with id: %s", userId);
        return userRepository.findByIdOptional(userId).orElseThrow(() ->
                new UnknownResourceException("User not found: " + userId)
        );
    }

    @Override
    @Transactional
    public Try<UserDto> assignRoleToUser(UUID userId, UUID roleId) {
        Log.infof("Assigning role with id: %s to user with id: %s", roleId, userId);
        return Try.of(() -> {
            UserEntity user = internalGetUserById(userId);
            RoleEntity role = roleService.internalGetRoleById(roleId);

            if (user.getRoles().stream().anyMatch(e -> e.getRoleId().equals(roleId)))
                throw new DuplicateResourceException("User already has this role");

            user.addRole(role);

            return userMapper.toDto(user);
        }).onFailure(ex -> {
            switch (ex){
                case UnknownResourceException e -> Log.warn(e.getMessage());
                case DuplicateResourceException e -> Log.warn(e.getMessage());
                default -> Log.errorf(ex,"Error assigning role with id: %s to user with id: %s", roleId, userId);
            }
        });
    }

    @Override
    @Transactional
    public Try<UserDto> unassignRoleFromUser(UUID userId, UUID roleId) {
        Log.infof("Unassigning role with id: %s from user with id: %s", roleId, userId);
        return Try.of(() -> {
            UserEntity user = internalGetUserById(userId);
            RoleEntity role = roleService.internalGetRoleById(roleId);

            user.removeRole(role);

            return userMapper.toDto(user);
        }).onFailure(ex -> {
            if (Objects.requireNonNull(ex) instanceof UnknownResourceException e) {
                Log.warn(e.getMessage());
            } else {
                Log.errorf(ex, "Error assigning role with id: %s to user with id: %s", roleId, userId);
            }
        });
    }

    @Override
    @Transactional
    public Try<UserDto> getUserById(UUID userId){
        Log.infof("Getting user with id: %s", userId);
        return Try.of(() -> internalGetUserById(userId))
                .map(userMapper::toDto)
                .onFailure(e -> {
                    if (Objects.requireNonNull(e) instanceof UnknownResourceException ex) {
                        Log.warn(ex.getMessage());
                    } else {
                        Log.errorf(e, "Error getting user with id: %s", userId);
                    }
                });

    }

    @Override
    public List<UserEntity> internalGetAllUsersFilteredByRole(UUID roleId){
        Log.infof("Getting all users filtered by role with id: %s", roleId);
        return userRepository.findByRole(roleId);
    }


    @Override
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

    @Override
    @Transactional
    public Try<UserDto> createUser(UserDto userDto){
        Log.infof("Creating user: %s", userDto);
        return Try.of(() -> {
            Log.debugf("Searching for already existing user with id: %s" , userDto.getUserId());
            if (userRepository.existsById(userDto.getUserId()))
                throw new DuplicateResourceException("User already exists: " + userDto.getUserId());

            Log.debugf("Persisting new user: %s", userDto);
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

    @Override
    public Boolean internalExistsById(UUID userId){
        Log.infof("Checking user existence with id: %s", userId);
        return userRepository.existsById(userId);
    }

    @Override
    public Boolean internalExistsByRole(UUID roleId) {
        return userRepository.existsByRole(roleId);
    }

    @Override
    @Transactional
    public Try<UserDto> updateUser(UserDto userDto){
        Log.infof("Updating user: %s", userDto);
        return Try.of(() -> {
            UserEntity user = internalGetUserById(userDto.getUserId());
            user = userMapper.partialDtoToEntity(user, userDto);
            return userMapper.toDto(user);
        }).onFailure(ex -> {
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
