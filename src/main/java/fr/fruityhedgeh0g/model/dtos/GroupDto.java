package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Value;

import java.util.Set;
import java.util.UUID;


@Value
public class GroupDto {
    @JsonView({GlobalViews.IdentifierOnly.class, SectorDto.Creation.class})
    UUID groupId;

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

    @JsonView(Extended.class)
    Set<UserDto> members;

    @JsonView(Basic.class)
    SectorDto sector;

    public interface Creation {}
    public interface Basic extends GlobalViews.IdentifierOnly {}
    public interface Extended extends Basic {}
}
