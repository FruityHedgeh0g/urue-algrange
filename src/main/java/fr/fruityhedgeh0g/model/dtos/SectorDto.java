package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.model.entities.GroupEntity;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Value
public class SectorDto {
    @JsonView(Views.IdentifierOnly.class)
    UUID sectorId;

//    @JsonView(Views.System.class)
//    LocalDateTime createdAt;
//
//    @JsonView(Views.System.class)
//    LocalDateTime updatedAt;
//
//    @JsonView(Views.System.class)
//    UUID updatedBy;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String name;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String description;

    @JsonView({Views.Extended.class,Views.Creation.class})
    @JsonSerialize(using = ViewSerializers.class)
    Set<GroupEntity> groups;

}
