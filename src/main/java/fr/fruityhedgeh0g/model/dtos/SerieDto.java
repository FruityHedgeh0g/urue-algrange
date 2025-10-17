package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.entities.EventEntity;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
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

    @JsonView(System.class)
    private UUID serieId;

    @JsonView(System.class)
    private LocalDateTime createdAt;

    @JsonView(System.class)
    private LocalDateTime updatedAt;

    @JsonView(System.class)
    private UUID updatedBy;

    @JsonView(Basic.class)
    private String name;

    @JsonView(Basic.class)
    private String description;

    @JsonView(Full.class)
    private Set<EventEntity> events;

    public interface System{}
    public interface Basic extends System {}
    public interface Full extends Basic {}
}
