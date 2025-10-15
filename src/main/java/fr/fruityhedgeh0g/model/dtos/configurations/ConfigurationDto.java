package fr.fruityhedgeh0g.model.dtos.configurations;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfigurationDto {
    private String name;

    private String value;
}
