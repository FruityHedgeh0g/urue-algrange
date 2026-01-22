package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.entities.GroupEntity;
import jakarta.persistence.*;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
public class SectorDto {

    @JsonView({Views.CreationResponse.class, Views.Basic.class})
    UUID sectorId;

    @JsonView({Views.Creation.class, Views.Basic.class})
    String name;

    @JsonView({Views.Creation.class, Views.Basic.class})
    String description;

    Set<GroupEntity> groups;

}
