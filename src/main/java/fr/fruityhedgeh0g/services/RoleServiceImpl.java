package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.RoleRepository;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
import fr.fruityhedgeh0g.utilities.mappers.RoleMapper;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Default
public class RoleServiceImpl implements RoleService {
    @Inject
    RoleRepository roleRepository;

    @Inject
    RoleMapper roleMapper;

    @Authenticated
    @Override
    public List<RoleDto> listAll() {
        return roleRepository.listAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Authenticated
    @Override
    public RoleDto getById(UUID roleId) {
        return roleMapper.toDto(
                roleRepository.findByIdOptional(roleId)
                        .orElseThrow(() -> new UnknownResourceException("Role not found: "+roleId))
        );

    }

    @Authenticated
    @Override
    @Transactional
    public RoleDto create(RoleDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName()))
            throw new DuplicateResourceException("This resource already exists in the system.");

        RoleEntity roleEntity = roleMapper.toEntity(roleDto);
        roleRepository.persist(roleEntity);

        return roleMapper.toDto(roleEntity);
    }

    @Authenticated
    @Override
    @Transactional
    public RoleDto update(RoleDto roleDto) {
        RoleEntity roleEntity = roleRepository.findByIdOptional(roleDto.getRoleId())
                .orElseThrow(() -> new UnknownResourceException("This resource is unknown in the system and cannot be updated."));

        if (!roleEntity.getName().equals(roleDto.getName()) && roleRepository.existsByName(roleDto.getName()))
            throw new DuplicateResourceException("A role with this name already exists in the system.");

        roleEntity = roleMapper.partialDtoToEntity(roleEntity,roleDto);
        roleRepository.persist(roleEntity);

        return roleMapper.toDto(roleEntity);
    }

    @Authenticated
    @Override
    @Transactional
    public void delete(UUID roleId) {
        //Todo: tester si le role si le role appartient à des users, refuser la suppression le cas échéant
        roleRepository.deleteById(roleId);
    }

//
//
//    @Override
//    @Transactional
//    public Try<List<RoleDto>> getAllRolesFilteredByRoleType(RoleTypeEnum[] filter) {
//        Log.infof("Getting all roles filtered by: %s", Arrays.toString(filter));
//        return Try.of(() -> roleRepository
//                .findByType(filter)
//                .stream()
//                .map(roleMapper::toDto)
//                .toList())
//                .onFailure(ex -> Log.error("Error getting all filtered roles", ex));
//    }
//
//    @Override
//    @Transactional
//    public Try<Void> deleteRole( UUID roleId) {
//        Log.infof("Deleting role with id: %s", roleId);
//        return Try.run(() -> {
//            Log.debugf("Checking if role with id: %s exists and retrieve it", roleId);
//            RoleEntity roleEntity = internalGetRoleById(roleId).getOrElseThrow(ex -> ex);
//
//            if (!roleEntity.getUsers().isEmpty())
//                throw new IllegalStateException("Cannot delete role with id: " + roleId + " as it is assigned to users");
//
//            roleRepository.delete(roleEntity);
//        }).onFailure(ex -> {
//            switch (ex){
//                case IllegalStateException e -> Log.warn(e.getMessage());
//                case UnknownResourceException e -> Log.warn(e.getMessage());
//                default -> Log.errorf(ex, "Error deleting role with id: %s", roleId);
//            }
//        });
//    }

}
