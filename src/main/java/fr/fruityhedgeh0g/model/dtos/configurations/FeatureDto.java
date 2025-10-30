package fr.fruityhedgeh0g.model.dtos.configurations;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@NoArgsConstructor
public class FeatureDto {
    @JsonView(Views.System.class)
    String name;

    @JsonView(Views.System.class)
    String description;

    @JsonView(Views.System.class)
    boolean isActive;
}
