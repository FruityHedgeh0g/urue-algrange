package fr.fruityhedgeh0g.model.dtos.configurations;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurationDto {
    @JsonView(Identifier.class)
    private UUID id;

    @JsonView(System.class)
    private LocalDateTime createdAt;

    @JsonView(System.class)
    private LocalDateTime updatedAt;

    @JsonView(System.class)
    private UUID updatedBy;

    private String name;

    private String value;

    public interface Identifier{}
    public interface System extends Identifier {}
    public interface Basic extends System {}
    public interface Full extends Basic {}
}
