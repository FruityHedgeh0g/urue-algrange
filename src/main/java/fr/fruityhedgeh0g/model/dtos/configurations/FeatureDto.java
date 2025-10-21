package fr.fruityhedgeh0g.model.dtos.configurations;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeatureDto {
    @JsonView(Views.identityOnly.class)
    private String name;

    @JsonView(Views.system.class)
    private String description;

    @JsonView(Views.system.class)
    private boolean isActive;
}
