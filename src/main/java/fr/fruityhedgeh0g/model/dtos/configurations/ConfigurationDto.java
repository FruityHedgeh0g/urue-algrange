package fr.fruityhedgeh0g.model.dtos.configurations;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.GlobalViews;
import lombok.Value;


@Value
public class ConfigurationDto {

//    @JsonView(Views.System.class)
//    LocalDateTime createdAt;
//
//    @JsonView(Views.System.class)
//    LocalDateTime updatedAt;
//
//    @JsonView(Views.System.class)
//    UUID updatedBy;

    @JsonView(GlobalViews.IdentifierOnly.class)
    String name;

    @JsonView(Basic.class)
    String value;

    public interface Basic extends GlobalViews.IdentifierOnly {}
}
