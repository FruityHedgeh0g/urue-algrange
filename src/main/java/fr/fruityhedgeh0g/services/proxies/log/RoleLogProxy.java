package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.RoleService;
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

@AllArgsConstructor
@Priority(200)
@Decorator
public class RoleLogProxy implements RoleService {
    @Inject
    @Delegate
    RoleService roleService;

    @Override
    public List<RoleDto> listAll() {
        Log.debugf("Trying to retrieve all roles.");
        return Try.of(roleService::listAll)
                .onSuccess(roles -> Log.debugf("%d roles retrieved.",roles.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving roles."))
                .get();
    }

    @Override
    public Optional<RoleDto> getById(UUID roleId) {
        Log.debugf("Trying to retrieve role by id %s.",roleId);
        return Try.of(() -> roleService.getById(roleId))
                .onSuccess(role -> {
                    if (role.isPresent())
                        Log.debugf("Role retrieved.");
                    else Log.debugf("There is no role with id %s.",roleId);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving role."))
                .get();

    }

    @Override
    public RoleDto create(RoleDto roleDto) {
        Log.debugf("Trying to create a new role : %s", roleDto.toString());
        return Try.of(() -> roleService.create(roleDto))
                .onSuccess(role -> Log.debugf("Role created."))
                .onFailure(t -> {
                    switch(t){
                        case DuplicateResourceException ex -> Log.errorf(t,"A role already exists for the id provided [%s].", roleDto.getRoleId());
                        default -> Log.errorf(t,"An error occurred while creating role.");
                    }
                })
                .get();

    }

    @Override
    public RoleDto update(RoleDto roleDto) {
        Log.debugf("Trying to update an existing role : %s", roleDto.toString());
        return Try.of(() -> roleService.update(roleDto))
                .onSuccess(role -> Log.debugf("Role updated."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(t,"There is no role with id %s.", roleDto.getRoleId());
                        case DuplicateResourceException ex -> Log.errorf(t, "A role already exists with this name [%s].", roleDto.getName());
                        default -> Log.errorf(t,"An error occurred while updating role.");
                    }
                })
                .get();
    }

    @Override
    public void delete(UUID roleId) {
        Log.debugf("Trying to delete role by id %s.",roleId);
        Try.run(() -> roleService.delete(roleId))
                .onSuccess(v -> Log.debugf("Role deleted."))
                .onFailure(t -> Log.errorf(t,"An error occurred during role deletion."))
                .get();

    }
}
