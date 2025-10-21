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
@Value
public class UserDto {
    @JsonView({Views.IdentityOnly.class, Views.Creation.class})
   UUID userId;

    @JsonView(Views.System.class)
    LocalDateTime createdAt;

    @JsonView(Views.System.class)
    LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    UUID updatedBy;

    @JsonView(Views.Basic.class)
    @JsonSerialize(using = ViewSerializers.class)
    GroupDto group;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<EventDto> organizedEvents;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<EventDto> participatedEvents;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<EventDto> createdEvents;

    @JsonView(Views.Minimal.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<RoleDto> roles;

}
