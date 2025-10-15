package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.dtos.configurations.ConfigurationDto;
import fr.fruityhedgeh0g.model.entities.configurations.ConfigurationEntity;
import fr.fruityhedgeh0g.repositories.ConfigurationRepository;
import fr.fruityhedgeh0g.utilities.mappers.ConfigurationMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@ApplicationScoped
public class ConfigurationService {

    @Inject
    ConfigurationRepository configurationRepository;

    @Inject
    ConfigurationMapper configurationMapper;

    public Try<ConfigurationDto> getConfigurationByName(String name) {
        Log.info("Getting configuration by name: " + name);
        return Try.of(() -> configurationRepository
                .findConfigurationByName(name)
                .orElseThrow(NoSuchElementException::new))
                .map(configurationMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Configuration not found: " + name);
                    }else {
                        Log.error("Error getting configuration by name: " + name, e);
                    }
                });
    }

    public Try<List<ConfigurationDto>> getAllConfigurations() {
        Log.info("Getting all configurations");
        return Try.of(() ->configurationRepository.listAll()
                .stream()
                .map(configurationMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all configurations", e));
    }

    public boolean updateConfiguration(ConfigurationDto dto) {
        Log.info("Updating configuration: " + dto.getName());
        return Try.run(() -> configurationRepository.updateConfiguration(configurationMapper.toEntity(dto)))
                .onFailure(e -> Log.error("Error updating configuration", e))
                .isSuccess();
    }
}
