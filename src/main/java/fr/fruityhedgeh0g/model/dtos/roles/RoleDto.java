package fr.fruityhedgeh0g.model.dtos.roles;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.EventDto;
import fr.fruityhedgeh0g.model.dtos.UserDto;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    @JsonView(System.class)
    private UUID roleId;

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

    public interface System{}
    public interface Basic extends System {}
    public interface Full extends Basic {}
}
