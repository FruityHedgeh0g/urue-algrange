package fr.fruityhedgeh0g.model.dtos.configurations;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.GlobalViews;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;


@Value
public class FeatureDto {
    @JsonView(GlobalViews.IdentifierOnly.class)
    String name;

    @JsonView(Basic.class)
    String description;

    @JsonView(Basic.class)
    boolean isActive;

    public interface Basic extends GlobalViews.IdentifierOnly {}
}
