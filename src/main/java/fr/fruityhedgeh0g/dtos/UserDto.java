package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.entities.EventEntity;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import jakarta.persistence.*;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
public class UserDto {

    @JsonView(Views.Creation.class)
    UUID userId;

    Set<RoleEntity> roles;

    GroupEntity group;

    Set<EventEntity> organizedEvents;

    Set<EventEntity> participatedEvents;

    Set<EventEntity> createdEvents;
}
