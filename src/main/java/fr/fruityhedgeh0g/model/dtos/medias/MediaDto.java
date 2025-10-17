package fr.fruityhedgeh0g.model.dtos.medias;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.UserDto;
import fr.fruityhedgeh0g.model.dtos.roles.RoleDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MediaDto {

    @JsonView(System.class)
    private UUID mediaId;

    @JsonView(System.class)
    private LocalDateTime createdAt;

    @JsonView(System.class)
    private LocalDateTime updatedAt;

    @JsonView(System.class)
    private UUID updatedBy;

    public interface System{}
    public interface Basic extends System {}
    public interface Full extends Basic {}
}
