package fr.fruityhedgeh0g.dtos.configurationDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.Views;
import lombok.Value;

@Value
public class ConfigurationDto {

    @JsonView({Views.Basic.class, Views.UpdateResponse.class})
    String name;

    @JsonView({Views.Basic.class, Views.UpdateResponse.class})
    String value;
}
