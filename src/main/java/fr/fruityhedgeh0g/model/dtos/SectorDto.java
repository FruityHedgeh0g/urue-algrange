package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
import lombok.Value;

import java.util.Set;
import java.util.UUID;


@Value
public class SectorDto {
    @JsonView(GlobalViews.IdentifierOnly.class)
    UUID sectorId;

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

    @JsonView({Extended.class,Creation.class})
    @JsonSerialize(using = ViewSerializers.class)
    Set<GroupDto> groups;

    public interface Creation {}
    public interface Basic extends GlobalViews.IdentifierOnly {}
    public interface Extended extends Basic {}

}
