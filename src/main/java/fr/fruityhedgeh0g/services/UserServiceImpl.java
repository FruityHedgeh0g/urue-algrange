package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@ApplicationScoped
@Default
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    //Using UUID to test the existence of the user is acceptable because it is based on an external system (Keycloak)

    @Override
    public List<UserDto> listAll() {
        return userRepository.listAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public Optional<UserDto> getById(UUID userId) {
        return userRepository.findByIdOptional(userId)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        if (userRepository.existsById(userDto.getUserId()))
                throw new DuplicateResourceException("This resource already exists in the system.");

        UserEntity userEntity = userMapper.toEntity(userDto);
        userRepository.persist(userEntity);

        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        UserEntity userEntity = userRepository.findByIdOptional(userDto.getUserId())
                .orElseThrow(() -> new UnknownResourceException("This resource is unknown in the system and cannot be updated."));


        userEntity = userMapper.partialDtoToEntity(userEntity,userDto);
        userRepository.persist(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        //Todo: non nécéssaire de supprimer des users.
        // On peut cependant imaginer tester si le user est utilisé dans une autre table et le supprimer dans le cas contraire
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<UserEntity> getEntityById(UUID userId) {
        return userRepository.findByIdOptional(userId);
    }

//
//    @Override
//    @Transactional
//    public Try<UserDto> assignRoleToUser(UUID userId, UUID roleId) {
//        Log.infof("Assigning role with id: %s to user with id: %s", roleId, userId);
//        return Try.of(() -> {
//            Log.debugf("Checking if user with id: %s exists and retrieve it", userId);
//            UserEntity user = internalGetUserById(userId).getOrElseThrow(ex -> ex);
//
//            Log.debugf("Checking if user with id: %s already has this role", userId);
//            if (user.getRoles().stream().anyMatch(e -> e.getRoleId().equals(roleId)))
//                throw new DuplicateResourceException("User already has this role");
//
//            RoleEntity role = roleService.internalGetRoleById(roleId).getOrElseThrow(ex -> ex);
//
//            user.addRole(role);
//
//            return userMapper.toDto(user);
//        }).onFailure(ex -> {
//            switch (ex){
//                case UnknownResourceException e -> Log.warn(e.getMessage());
//                case DuplicateResourceException e -> Log.warn(e.getMessage());
//                default -> Log.errorf(ex,"Error assigning role with id: %s to user with id: %s", roleId, userId);
//            }
//        });
//    }
//
//    @Override
//    @Transactional
//    public Try<UserDto> unassignRoleFromUser(UUID userId, UUID roleId) {
//        Log.infof("Unassigning role with id: %s from user with id: %s", roleId, userId);
//        return Try.of(() -> {
//            Log.debugf("Checking if user with id: %s exists and retrieve it", userId);
//            UserEntity user = internalGetUserById(userId).getOrElseThrow(ex -> ex);
//
//            Log.debugf("Checking if role with id: %s exists and retrieve it", roleId);
//            RoleEntity role = user.getRoles().stream().filter(e -> e.getRoleId().equals(roleId)).findFirst()
//                    .orElseThrow(() -> new UnknownResourceException("Role not found"));
//
//            user.removeRole(role);
//
//            return userMapper.toDto(user);
//        }).onFailure(ex -> {
//            switch(ex) {
//                case UnknownResourceException e -> Log.warn(e.getMessage());
//                case DuplicateResourceException e -> Log.warn(e.getMessage());
//                default -> Log.errorf(ex, "Error unassigning role with id: %s from user with id: %s", roleId, userId);
//            }
//        });
//    }

}
