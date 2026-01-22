package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Value;

@Value
public class ConfigurationDto {

    @JsonView(Views.Basic.class)
    String name;

    @JsonView(Views.Basic.class)
    String value;
}
