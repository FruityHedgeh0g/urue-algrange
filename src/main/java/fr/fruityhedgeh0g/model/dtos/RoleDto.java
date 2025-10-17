package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    @JsonView(System.class)
    private UUID roleId;

    @JsonView(Identifier.class)
    private LocalDateTime createdAt;

    @JsonView(System.class)
    private LocalDateTime updatedAt;

    @JsonView(System.class)
    private UUID updatedBy;

    @JsonView(Basic.class)
    private String name;

    @JsonView(Basic.class)
    private String description;

    @JsonView(System.class)
    private String roleType;

    @JsonView(Full.class)
    private Set<UserDto> users;

    public interface Identifier{}
    public interface System extends Identifier {}
    public interface Basic extends System {}
    public interface Full extends Basic {}
}
