package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface PublicConfigurationService {
    List<ConfigurationDto> listAll();
    Optional<ConfigurationDto> getByName(@NotNull String name);
    ConfigurationDto update(@NotNull @Valid ConfigurationDto configurationDto);

//    Try<List<ConfigurationDto>> getAllConfigurations();
//    Try<ConfigurationDto> getConfigurationByName(@NotBlank String name);
//    Try<ConfigurationDto> updateConfiguration(@NotNull @Valid ConfigurationDto configurationDto);
}
