package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.utilities.logging.Logged;

import fr.fruityhedgeh0g.dtos.userDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.enums.RoleEnum;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.ForbiddenRoleChangeException;
import fr.fruityhedgeh0g.exceptions.NotImplementedYetException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.keycloak.KeycloakRoleMirror;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.services.interfaces.UserService;
import fr.fruityhedgeh0g.utilities.mappers.UserMapper;
import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
@Logged
@ApplicationScoped
@Default
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    @Inject
    KeycloakRoleMirror keycloakRoleMirror;

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
    public UserDto changeRole(UUID actorId, UUID personId, RoleEnum role) {
        // Committed on its own before the Keycloak call, so a mirror failure cannot roll it back (ADR 0002)
        UserDto changed = QuarkusTransaction.requiringNew().call(() -> {
            RoleEnum actorRole = userRepository.findByIdOptional(actorId)
                    .map(UserEntity::getRole)
                    .orElseThrow(() -> new ForbiddenRoleChangeException("Unknown actor: " + actorId));
            if (actorId.equals(personId))
                throw new ForbiddenRoleChangeException("Nobody changes their own Role.");

            UserEntity person = userRepository.findByIdOptional(personId)
                    .orElseThrow(() -> new UnknownResourceException("User not found: " + personId));
            if (!actorRole.maySetRole(person.getRole(), role))
                throw new ForbiddenRoleChangeException(actorRole.id() + " cannot set " + person.getRole().id() + " to " + role.id());

            person.setRole(role);
            return userMapper.toDto(person);
        });

        try {
            keycloakRoleMirror.setRoleGroup(personId, role);
        } catch (RuntimeException e) {
            // ADR 0002: the database Role still applies; the group is repaired on the next Role change
            Log.warnf(e, "Could not mirror Role %s of %s to Keycloak", role.id(), personId);
        }
        return changed;
    }

    @Override
    @Transactional
    public UserDto doCreate(UserDto userDto) {
        if (userRepository.existsById(userDto.getUserId()))
                throw new DuplicateResourceException("This resource already exists in the system.");

        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setRole(RoleEnum.BENEVOLE);
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
