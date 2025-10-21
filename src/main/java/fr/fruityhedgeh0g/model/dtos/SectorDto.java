package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.model.entities.GroupEntity;
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
public class SectorDto {
    @JsonView(Views.identityOnly.class)
    private UUID sectorId;

    @JsonView(Views.system.class)
    private LocalDateTime createdAt;

    @JsonView(Views.system.class)
    private LocalDateTime updatedAt;

    @JsonView(Views.system.class)
    private UUID updatedBy;

    @JsonView({Views.minimal.class,Views.creation.class})
    private String name;

    @JsonView({Views.basic.class,Views.creation.class})
    private String description;

    @JsonView(Views.full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<GroupEntity> groups;

}
