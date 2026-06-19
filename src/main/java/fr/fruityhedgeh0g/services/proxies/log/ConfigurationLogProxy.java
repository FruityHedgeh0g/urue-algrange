package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.ConfigurationService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Priority(200)
@Decorator
public class ConfigurationLogProxy implements ConfigurationService{

    @Inject
    @Delegate
    ConfigurationService configurationService;

    @Override
    public List<ConfigurationDto> listAll() {
        Log.debugf("Retrieving all configurations...");
        return Try.of(configurationService::listAll)
                .onSuccess(configs -> Log.debugf("%d configurations retrieved.",configs.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving configurations."))
                .get();
    }

    @Override
    public Optional<ConfigurationDto> getByName(String name) {
        Log.debugf("Retrieving configuration by name %s...",name);
        return Try.of(() -> configurationService.getByName(name))
                .onSuccess(configuration -> {
                    if (configuration.isPresent())
                        Log.debugf("Configuration retrieved: "+configuration.toString());
                    else Log.debugf("Configuration with name %s not found.",name);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving configuration."))
                .get();
    }

    @Override
    public ConfigurationDto update(ConfigurationDto configurationDto) {
        Log.debugf("Updating an existing configuration: %s", configurationDto.toString());
        return Try.of(() -> configurationService.update(configurationDto))
                .onSuccess(config -> Log.debugf("Configuration updated."))
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"Configuration %s not found.", configurationDto.getName());
                        default -> Log.errorf(t,"An error occurred while updating configuration.");
                    }
                })
                .get();
    }
}
