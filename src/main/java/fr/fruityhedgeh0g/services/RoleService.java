package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateEntityException;
import fr.fruityhedgeh0g.model.dtos.RoleDto;
import fr.fruityhedgeh0g.model.entities.roles.RoleEntity;
import fr.fruityhedgeh0g.repositories.RoleRepository;
import fr.fruityhedgeh0g.utilities.mappers.RoleMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class RoleService {
    @Inject
    RoleRepository roleRepository;

    @Inject
    RoleMapper roleMapper;

    public Try<List<RoleDto>> getAllRoles() {
        Log.info("Getting all roles");
        return null;
    }

    public Try<RoleDto> getRoleById(@NotNull UUID roleId) {
        Log.info("Getting role with id: " + roleId);
        return Try.of(() -> roleRepository
                        .findByIdOptional(roleId)
                        .orElseThrow(NoSuchElementException::new))
                .map(roleMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Role not found: " + roleId);
                    }else {
                        Log.error("Error getting role with id: " + roleId, e);
                    }
                });
    }

    public Try<RoleDto> createRole(@NotNull RoleDto roleDto) {
        return Try.of(() -> {
            Log.debug("Searching for already existing role with name: " + roleDto.getName());
            if (roleRepository.existsByName(roleDto.getName())) {
                throw new DuplicateEntityException();
            }

            Log.debug("Creating role: " + roleDto.getName());
            RoleEntity roleEntity = roleMapper.toEntity(roleDto);
            roleRepository.persist(roleEntity);

            Log.debug("User created, retrieving up-to-date role infos: " + roleEntity.getRoleId());
            return roleMapper.toDto(
                    roleRepository
                            .findByIdOptional(roleEntity.getRoleId())
                            .orElseThrow(NoSuchElementException::new)
            );
        }).onFailure(e -> {
            if (e instanceof DuplicateEntityException) {
                Log.warn("Role already exists: " + roleDto.getName());
            }else {
                Log.error("Error creating role with name: " + roleDto.getName(), e);
            }
        });
    }

    public Try<RoleDto> updateRole(@NotNull RoleDto roleDto) {
        Log.info("Updating role: " + roleDto.getRoleId());
        return null;
    }

    public void deleteRole(@NotNull UUID roleId) {
        Log.info("Deleting role with id: " + roleId);
        Try.run(() -> roleRepository.deleteById(roleId))
                .onFailure(e -> Log.error("Error deleting role with id: " + roleId, e));
    }
}
