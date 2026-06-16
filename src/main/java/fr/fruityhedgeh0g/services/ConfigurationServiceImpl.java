package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.entities.configurations.ConfigurationEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.ConfigurationRepository;
import fr.fruityhedgeh0g.services.interfaces.ConfigurationService;
import fr.fruityhedgeh0g.utilities.mappers.ConfigurationMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class ConfigurationServiceImpl implements ConfigurationService {

    @Inject
    ConfigurationRepository configurationRepository;

    @Inject
    ConfigurationMapper configurationMapper;

    @Override
    public Try<List<ConfigurationDto>> listAll() {
        return null;
    }

    @Override
    public Try<ConfigurationDto> getByName(String name) {
        return null;
    }

    @Override
    public Try<ConfigurationDto> update(ConfigurationDto configurationDto) {
        return null;
    }

//    @Transactional
//    public Try<ConfigurationDto> getConfigurationByName( String name) {
//        Log.info("Getting configuration by name: " + name);
//        return Try.of(() -> configurationRepository
//                .findByIdOptional(name)
//                .orElseThrow(() -> new UnknownResourceException("Configuration not found: " + name)))
//                .map(configurationMapper::toDto)
//                .onFailure(e -> {
//                    if (e instanceof UnknownResourceException) {
//                        Log.warn("Configuration not found: " + name);
//                    }else {
//                        Log.error("Error getting configuration by name: " + name, e);
//                    }
//                });
//    }
//
//    @Transactional
//    public Try<List<ConfigurationDto>> getAllConfigurations() {
//        Log.info("Getting all configurations");
//        return Try.of(() ->configurationRepository.findAll()
//                .stream()
//                .map(configurationMapper::toDto)
//                .toList())
//                .onFailure(e -> Log.error("Error getting all configurations", e));
//    }
//
//    @Transactional
//    public Try<ConfigurationDto> updateConfiguration( ConfigurationDto dto) {
//        Log.info("Updating configuration: " + dto.getName());
//        return Try.of(() -> {
//            Log.debugf("Checking if configuration with name: %s already exists", dto.getName());
//            ConfigurationEntity configuration = configurationRepository.findByIdOptional(dto.getName())
//                    .orElseThrow(() -> new UnknownResourceException("Configuration not found: " + dto.getName()));
//
//            configurationMapper.partialDtoToEntity(configuration, dto);
//
//            return configurationMapper.toDto(configuration);
//        }).onFailure(e -> {
//            if (e instanceof UnknownResourceException) {
//                Log.warn(e.getMessage());
//            } else {
//                Log.error("Error updating configuration: " + dto.getName(), e);
//            }
//        });
//    }
}
