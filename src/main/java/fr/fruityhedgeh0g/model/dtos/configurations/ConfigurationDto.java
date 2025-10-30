package fr.fruityhedgeh0g.model.dtos.configurations;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurationDto {

    @JsonView(Views.System.class)
    LocalDateTime createdAt;

    @JsonView(Views.System.class)
    LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    UUID updatedBy;

    @JsonView(Views.System.class)
    String name;

    @JsonView(Views.System.class)
    String value;

}
