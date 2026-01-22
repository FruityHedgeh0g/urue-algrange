package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.ConfigurationDto;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.ConfigurationRepository;
import fr.fruityhedgeh0g.utilities.mappers.ConfigurationMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@ApplicationScoped
public class ConfigurationService {

    @Inject
    ConfigurationRepository configurationRepository;

    @Inject
    ConfigurationMapper configurationMapper;

    @Transactional
    public Try<ConfigurationDto> getConfigurationByName(@NotBlank String name) {
        Log.info("Getting configuration by name: " + name);
        return Try.of(() -> configurationRepository
                .findByIdOptional(name)
                .orElseThrow(() -> new UnknownResourceException("Configuration not found: " + name)))
                .map(configurationMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof UnknownResourceException) {
                        Log.warn("Configuration not found: " + name);
                    }else {
                        Log.error("Error getting configuration by name: " + name, e);
                    }
                });
    }

    @Transactional
    public Try<List<ConfigurationDto>> getAllConfigurations() {
        Log.info("Getting all configurations");
        return Try.of(() ->configurationRepository.findAll()
                .stream()
                .map(configurationMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all configurations", e));
    }

    //TODO : Développer l'update
    @Transactional
    public Try<ConfigurationDto> updateConfiguration(@NotNull ConfigurationDto dto) {
        Log.info("Updating configuration: " + dto.getName());
//        return Try.run(() -> configurationRepository.updateConfiguration(configurationMapper.toEntity(dto)))
//                .onFailure(e -> Log.error("Error updating configuration", e))
//                .isSuccess();
        return null;
    }
}
