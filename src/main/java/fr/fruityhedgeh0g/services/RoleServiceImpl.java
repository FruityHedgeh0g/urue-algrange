package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
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

import java.util.List;
import java.util.NoSuchElementException;
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

    @Transactional
    public Try<List<RoleDto>> getAllRoles() {
        return null;
    }

    @Override
    public Try<List<RoleDto>> getAllRolesFiltered(String filter) {
        return null;
    }

    @Transactional
    public Try<RoleDto> getRoleById( UUID roleId) {
        return null;
    }

    @Transactional
    public Try<RoleDto> createRole( RoleDto roleDto) {
        return null;
    }

    //TODO : Développer l'update
    @Transactional
    public Try<RoleDto> updateRole( RoleDto roleDto) {
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    @Transactional
    public Try<Void> deleteRole( UUID roleId) {
        return null;
    }
}
