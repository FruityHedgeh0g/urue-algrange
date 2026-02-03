package fr.fruityhedgeh0g.dtos.ConfigurationDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.Views;
import lombok.Value;

@Value
public class ConfigurationDto {

    String name;

    String value;
}
