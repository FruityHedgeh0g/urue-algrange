package fr.fruityhedgeh0g.model.dtos.configurations;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeatureDto {

    private String name;

    private String description;

    private boolean isActive;
}
