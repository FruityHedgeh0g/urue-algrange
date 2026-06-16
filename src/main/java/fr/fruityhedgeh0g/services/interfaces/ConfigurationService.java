package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface ConfigurationService {
    Try<List<ConfigurationDto>> listAll();
    Try<ConfigurationDto> getByName(@NotNull String name);
    Try<ConfigurationDto> update(@NotNull @Valid ConfigurationDto configurationDto);

//    Try<List<ConfigurationDto>> getAllConfigurations();
//    Try<ConfigurationDto> getConfigurationByName(@NotBlank String name);
//    Try<ConfigurationDto> updateConfiguration(@NotNull @Valid ConfigurationDto configurationDto);
}
