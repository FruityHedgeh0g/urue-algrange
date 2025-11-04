package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Value;

import java.util.UUID;


@Value
public class RoleDto {
    @JsonView(GlobalViews.IdentifierOnly.class)
    UUID roleId;

//    @JsonView(Views.System.class)
//    LocalDateTime createdAt;
//
//    @JsonView(Views.System.class)
//    LocalDateTime updatedAt;
//
//    @JsonView(Views.System.class)
//    UUID updatedBy;

    @JsonView({Basic.class, Creation.class})
    String name;

    @JsonView({Basic.class, Creation.class})
    String description;

    @JsonView({Basic.class, Creation.class})
    String roleType;

//    @JsonView(Views.Full.class)
//    @JsonSerialize(using = ViewSerializers.class)
//    Set<UserDto> users;

    public interface Creation {}
    public interface Basic extends GlobalViews.IdentifierOnly {}

}
