package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
public class UserDto {
    @JsonView({GlobalViews.IdentifierOnly.class, Creation.class})
   UUID userId;

//    @JsonView(Views.System.class)
//    LocalDateTime createdAt;
//
//    @JsonView(Views.System.class)
//    LocalDateTime updatedAt;
//
//    @JsonView(Views.System.class)
//    UUID updatedBy;

    @JsonView(Basic.class)
    @JsonSerialize(using = ViewSerializers.class)
    GroupDto group;

    @JsonView(Extended.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<EventDto> organizedEvents;

    @JsonView(Extended.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<EventDto> participatedEvents;

    @JsonView(Extended.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<EventDto> createdEvents;

    @JsonView(Extended.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<RoleDto> roles;

    public interface Creation {}
    public interface Basic extends GlobalViews.IdentifierOnly {}
    public interface Extended extends Basic {}
}
