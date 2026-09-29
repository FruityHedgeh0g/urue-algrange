package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicConfigurationService;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfigurationService extends PublicConfigurationService {

//    Try<List<ConfigurationDto>> getAllConfigurations();
//    Try<ConfigurationDto> getConfigurationByName(@NotBlank String name);
//    Try<ConfigurationDto> updateConfiguration(@NotNull @Valid ConfigurationDto configurationDto);
}
