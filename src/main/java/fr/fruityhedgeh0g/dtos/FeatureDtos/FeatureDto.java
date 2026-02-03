package fr.fruityhedgeh0g.dtos.FeatureDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.Views;
import lombok.Value;

@Value
public class FeatureDto {
    String name;

    String description;

    boolean isActive;
}
