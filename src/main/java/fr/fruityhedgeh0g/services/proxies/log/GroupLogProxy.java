package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(200)
@Decorator
public class GroupLogProxy implements GroupService {

    @Inject
    @Delegate
    GroupService groupService;


    @Override
    public List<GroupDto> listAll() {
        Log.debugf("Trying to retrieve all groups.");
        return Try.of(groupService::listAll)
                .onSuccess(groups -> Log.debugf("%d groups retrieved.",groups.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving groups."))
                .get();
    }

    @Override
    public Optional<GroupDto> getById(UUID groupId) {
        Log.debugf("Trying to retrieve group by id %s.",groupId);
        return Try.of(() -> groupService.getById(groupId))
                .onSuccess(group -> {
                    if (group.isPresent())
                        Log.debugf("Group retrieved.");
                    else Log.debugf("There is no group with id %s.",groupId);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving group."))
                .get();
    }

    @Override
    public GroupDto create(GroupDto groupDto) {
        Log.debugf("Trying to create a new group : %s", groupDto.toString());
        return Try.of(() -> groupService.create(groupDto))
                .onSuccess(group -> Log.debugf("Group created."))
                .onFailure(t -> {
                    switch(t){
                        case DuplicateResourceException ex -> Log.errorf(ex,"A group already exists for the id provided [%s].", groupDto.getGroupId());
                        default -> Log.errorf(t,"An error occurred while creating group.");
                    }
                })
                .get();
    }

    @Override
    public GroupDto update(GroupDto groupDto) {
        Log.debugf("Trying to update an existing group : %s", groupDto.toString());
        return Try.of(() -> groupService.update(groupDto))
                .onSuccess(group -> Log.debugf("Group updated."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"There is no group with id %s.", groupDto.getGroupId());
                        case DuplicateResourceException ex -> Log.errorf(ex, "A group already exists with this name [%s].", groupDto.getName());
                        default -> Log.errorf(t,"An error occurred while updating group.");
                    }
                })
                .get();
    }

    @Override
    public void delete(UUID groupId) {
        Log.debugf("Trying to delete group by id %s.",groupId);
        Try.run(() -> groupService.delete(groupId))
                .onSuccess(v -> Log.debugf("Group deleted."))
                .onFailure(t -> Log.errorf(t,"An error occurred during group deletion."))
                .get();
    }
}
