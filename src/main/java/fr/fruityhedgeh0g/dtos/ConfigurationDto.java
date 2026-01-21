package fr.fruityhedgeh0g.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Value;

@Value
public class ConfigurationDto {

    String name;

    String value;
}
