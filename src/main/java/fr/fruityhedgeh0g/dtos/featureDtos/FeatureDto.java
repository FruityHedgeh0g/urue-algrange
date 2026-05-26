package fr.fruityhedgeh0g.dtos.featureDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.Views;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FeatureDto {
    @JsonView({Views.Basic.class, Views.UpdateResponse.class})
    String name;

    @JsonView({Views.Basic.class, Views.UpdateResponse.class})
    String description;

    @JsonView({Views.Basic.class, Views.UpdateResponse.class})
    Boolean isActive;
}
