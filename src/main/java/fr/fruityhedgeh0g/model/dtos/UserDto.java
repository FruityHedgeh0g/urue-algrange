package fr.fruityhedgeh0g.model.dtos;

import fr.fruityhedgeh0g.model.entities.EventEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserDto {

    private UUID userId;

    //private Set<RoleEntity> accreditations;

    private Set<EventDto> organizedEvents;

    private Set<EventDto> participatedEvents;

    private Set<EventDto> createdEvents;
}
