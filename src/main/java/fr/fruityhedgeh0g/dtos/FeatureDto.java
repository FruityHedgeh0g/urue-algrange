package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import lombok.Value;

@Value
public class FeatureDto {
    @JsonView(Views.Basic.class)
    String name;

    @JsonView(Views.Basic.class)
    String description;

    @JsonView(Views.Basic.class)
    boolean isActive;
}
