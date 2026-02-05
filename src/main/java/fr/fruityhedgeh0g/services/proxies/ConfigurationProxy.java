package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.services.interfaces.ConfigurationService;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;

@Identifier("configurationProxy")
@AllArgsConstructor
@ApplicationScoped
public class ConfigurationProxy implements ConfigurationService {
    @Inject
    ConfigurationService configurationService;

    @Override
    public Try<List<ConfigurationDto>> getAllConfigurations() {
        return null;
    }

    @Override
    public Try<ConfigurationDto> getConfigurationByName(String name) {
        return null;
    }

    @Override
    public Try<ConfigurationDto> updateConfiguration(ConfigurationDto configurationDto) {
        return null;
    }
}
