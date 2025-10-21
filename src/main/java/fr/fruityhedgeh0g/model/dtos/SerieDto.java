package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.model.entities.EventEntity;
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
public class SerieDto {
    @JsonView(Views.IdentityOnly.class)
    private UUID serieId;

    @JsonView(Views.System.class)
    private LocalDateTime createdAt;

    @JsonView(Views.System.class)
    private LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    private UUID updatedBy;

    @JsonView({Views.Minimal.class, Views.Creation.class})
    private String name;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String description;

    @JsonView({Views.Full.class, Views.Creation.class})
    @JsonSerialize(using = ViewSerializers.class)
    private Set<EventEntity> events;

}
