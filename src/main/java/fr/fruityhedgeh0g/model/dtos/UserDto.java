package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    @JsonView({Views.IdentityOnly.class, Views.Creation.class})
    private UUID userId;

    @JsonView(Views.System.class)
    private LocalDateTime createdAt;

    @JsonView(Views.System.class)
    private LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    private UUID updatedBy;

    @JsonView(Views.Basic.class)
    @JsonSerialize(using = ViewSerializers.class)
    private GroupDto group;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<EventDto> organizedEvents;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<EventDto> participatedEvents;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<EventDto> createdEvents;

    @JsonView(Views.Minimal.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<RoleDto> roles;

}
