package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    @JsonView({Views.System.class,Views.Creation.class})
    UUID userId;

    @JsonView(Views.System.class)
    LocalDateTime createdAt;

    @JsonView(Views.System.class)
    LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    UUID updatedBy;

    @JsonView(Views.Basic.class)
    GroupDto group;

    @JsonView(Views.Extended.class)
    Set<EventDto> organizedEvents;

    @JsonView(Views.Extended.class)
    Set<EventDto> participatedEvents;

    @JsonView(Views.Extended.class)
    Set<EventDto> createdEvents;

    @JsonView(Views.Basic.class)
    Set<RoleDto> roles;

}


