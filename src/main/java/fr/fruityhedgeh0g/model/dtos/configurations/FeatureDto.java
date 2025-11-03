package fr.fruityhedgeh0g.model.dtos.configurations;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;


@Value
public class FeatureDto {
    @JsonView(Views.IdentifierOnly.class)
    String name;

    @JsonView(Views.Basic.class)
    String description;

    @JsonView(Views.Basic.class)
    boolean isActive;
}
