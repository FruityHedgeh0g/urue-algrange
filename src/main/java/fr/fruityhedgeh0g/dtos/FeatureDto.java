package fr.fruityhedgeh0g.dtos;

import jakarta.persistence.Column;
import lombok.Value;

@Value
public class FeatureDto {
    String name;

    String description;

    boolean isActive;
}
