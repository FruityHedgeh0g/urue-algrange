package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.enums.RoleTypeEnum;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import fr.fruityhedgeh0g.repositories.RoleRepository;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import fr.fruityhedgeh0g.utilities.mappers.RoleMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class RoleServiceImpl implements RoleService {
    @Inject
    RoleRepository roleRepository;

    @Inject
    RoleMapper roleMapper;

    @Override
    @Transactional
    public Try<List<RoleDto>> getAllRoles() {
        Log.info("Getting all roles");
        return Try.of(() -> roleRepository
                .findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList())
                .onFailure(ex -> Log.error("Error getting all roles", ex));
    }


    @Override
    @Transactional
    public Try<List<RoleDto>> getAllRolesFilteredByRoleType(RoleTypeEnum[] filter) {
        Log.infof("Getting all roles filtered by: %s", Arrays.toString(filter));
        return Try.of(() -> roleRepository
                .findByType(filter)
                .stream()
                .map(roleMapper::toDto)
                .toList())
                .onFailure(ex -> Log.error("Error getting all filtered roles", ex));
    }

    @Transactional
    @Override
    public Try<RoleDto> getRoleById( UUID roleId) {
        Log.infof("Getting role with id: %s", roleId);
        return Try.of(() -> internalGetRoleById(roleId).getOrElseThrow(ex -> ex))
                .map(roleMapper::toDto)
                .onFailure(ex -> {
                    if (ex instanceof UnknownResourceException e) {
                        Log.warn(e.getMessage());
                    } else {
                        Log.errorf(ex, "Error getting role with id: %s", roleId);
                    }
                });
    }

    @Override
    public Try<RoleEntity> internalGetRoleById(UUID roleId) throws UnknownResourceException {
        Log.infof("Getting role with id: %s", roleId);
        return Try.of(() ->roleRepository.findByIdOptional(roleId).orElseThrow(() ->
                new UnknownResourceException("Role not found")));
    }

    @Transactional
    public Try<RoleDto> createRole( RoleDto roleDto) {
        Log.infof("Creating role: %s", roleDto);
        return Try.of(() -> {
            Log.debugf("Checking if role with name: %s already exists", roleDto.getName());
            if (roleRepository.existsByName(roleDto.getName()))
                throw new DuplicateResourceException("Role already exists: " + roleDto.getName());

            RoleEntity roleEntity = roleMapper.toEntity(roleDto);

            Log.debug("Persisting new role: " + roleEntity.getRoleId());
            roleRepository.persist(roleEntity);

            return roleMapper.toDto(roleEntity);
        }).onFailure(ex -> {
            if (ex instanceof DuplicateResourceException e) {
                Log.warn(e.getMessage());
            } else {
                Log.errorf(ex, "Error creating role: %s", roleDto);
            }
        });
    }


    @Transactional
    public Try<RoleDto> updateRole( RoleDto roleDto) {
        Log.infof("Updating role: %s", roleDto);
        return Try.of(() -> {
            Log.debugf("Checking if role with id: %s exists and retrieve it", roleDto.getRoleId());
            RoleEntity roleEntity = internalGetRoleById(roleDto.getRoleId()).getOrElseThrow(ex -> ex);

            Log.debugf("Checking if role with name: %s already exists", roleDto.getName());
            if (!roleDto.getName().equals(roleEntity.getName()) && roleRepository.existsByName(roleDto.getName()))
                throw new DuplicateResourceException("Role already exists: " + roleDto.getName());

            roleMapper.partialDtoToEntity(roleEntity, roleDto);

            return roleMapper.toDto(roleEntity);
        }).onFailure( ex -> {
                switch (ex){
                    case DuplicateResourceException e -> Log.warn(e.getMessage());
                    case UnknownResourceException e -> Log.warn(e.getMessage());
                    default -> Log.errorf(ex, "Error updating role: %s", roleDto);
                }
        }
        );
    }

    @Override
    @Transactional
    public Try<Void> deleteRole( UUID roleId) {
        Log.infof("Deleting role with id: %s", roleId);
        return Try.run(() -> {
            Log.debugf("Checking if role with id: %s exists and retrieve it", roleId);
            RoleEntity roleEntity = internalGetRoleById(roleId).getOrElseThrow(ex -> ex);

            if (!roleEntity.getUsers().isEmpty())
                throw new IllegalStateException("Cannot delete role with id: " + roleId + " as it is assigned to users");

            roleRepository.delete(roleEntity);
        }).onFailure(ex -> {
            switch (ex){
                case IllegalStateException e -> Log.warn(e.getMessage());
                case UnknownResourceException e -> Log.warn(e.getMessage());
                default -> Log.errorf(ex, "Error deleting role with id: %s", roleId);
            }
        });
    }

}
