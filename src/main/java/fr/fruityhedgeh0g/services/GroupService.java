package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.dtos.GroupDto;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class GroupService {
    @Inject
    GroupRepository groupRepository;

    @Inject
    GroupMapper groupMapper;

    public Try<List<GroupDto>> getAllGroups(){
        Log.info("Getting all groups");
        return Try.of(() -> groupRepository
                .findAll()
                .stream()
                .map(groupMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all groups", e));
    }

    public Try<GroupDto> getGroupById(@NotNull UUID groupId){
        Log.info("Getting group with id: " + groupId);
        return Try.of(() -> groupRepository
                        .findByIdOptional(groupId)
                        .orElseThrow(NoSuchElementException::new))
                .map(groupMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Group not found: " + groupId);
                    }else {
                        Log.error("Error getting group with id: " + groupId, e);
                    }
                });
    }

    public Try<GroupDto> createGroup(@NotNull GroupDto groupDto){
        Log.info("Creating group: " + groupDto.getGroupId());
        return null;
    }

    public Try<GroupDto> updateGroup(@NotNull GroupDto groupDto){
        Log.info("Updating group: " + groupDto.getGroupId());
        return null;
    }

    public void deleteGroup(@NotNull UUID groupId){
        Log.info("Deleting group with id: " + groupId);

    }




}
