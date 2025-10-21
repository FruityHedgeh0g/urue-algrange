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
    @JsonView({Views.identityOnly.class, Views.creation.class})
    private UUID userId;

    @JsonView(Views.system.class)
    private LocalDateTime createdAt;

    @JsonView(Views.system.class)
    private LocalDateTime updatedAt;

    @JsonView(Views.system.class)
    private UUID updatedBy;

    @JsonView(Views.basic.class)
    @JsonSerialize(using = ViewSerializers.class)
    private GroupDto group;

    @JsonView(Views.full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<EventDto> organizedEvents;

    @JsonView(Views.full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<EventDto> participatedEvents;

    @JsonView(Views.full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<EventDto> createdEvents;

    @JsonView(Views.minimal.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<RoleDto> roles;

}
