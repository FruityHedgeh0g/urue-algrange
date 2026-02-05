package fr.fruityhedgeh0g.dtos.featureDtos;

import lombok.Value;

@Value
public class FeatureDto {
    String name;

    String description;

    boolean isActive;
}
