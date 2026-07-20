package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.NotImplementedYetException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.*;

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
    public UserDto getById(UUID userId) {
        return userMapper.toDto(
                userRepository.findByIdOptional(userId)
                        .orElseThrow(() -> new UnknownResourceException("User not found: "+userId))
        );

    }

    @Override
    @Transactional
    public UserDto doCreate(UserDto userDto) {
        if (userRepository.existsById(userDto.getUserId()))
                throw new DuplicateResourceException("This resource already exists in the system.");

        UserEntity userEntity = userMapper.toEntity(userDto);
        userRepository.persist(userEntity);

        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto doUpdate(UserDto userDto) {
        UserEntity userEntity = userRepository.findByIdOptional(userDto.getUserId())
                .orElseThrow(() -> new UnknownResourceException("This resource is unknown in the system and cannot be updated."));


        userEntity = userMapper.partialDtoToEntity(userEntity,userDto);
        userRepository.persist(userEntity);
        return userMapper.toDto(userEntity);
    }


    //Todo: il va falloir par principe permettre la suppression d'un user. Nous devons permettre à chacun de supprimer ses traces.
    @Override
    @Transactional
    public void doDelete(UUID userId) {
        // On peut cependant imaginer tester si le user est utilisé dans une autre table et le supprimer dans le cas contraire
        //userRepository.deleteById(userId);
        throw new NotImplementedYetException(this.getClass().getSimpleName());
    }

    @Override
    public Optional<UserEntity> doGetEntityById(UUID userId) {
        return userRepository.findByIdOptional(userId);
    }

}
