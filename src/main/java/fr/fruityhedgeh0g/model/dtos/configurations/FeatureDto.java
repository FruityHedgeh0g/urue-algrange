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
    @JsonView(Views.IdentityOnly.class)
    private String name;

    @JsonView(Views.System.class)
    private String description;

    @JsonView(Views.System.class)
    private boolean isActive;
}
