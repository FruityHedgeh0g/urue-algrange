package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.entities.SectorEntity;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class GroupDto {

    @JsonView(Identifier.class)
    private UUID groupId;

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
    private Set<UserEntity> members;

    @JsonView(Full.class)
    private SectorEntity sector;

    public interface Identifier{}
    public interface System extends Identifier {}
    public interface Basic extends System {}
    public interface Full extends Basic {}
}
