package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.services.interfaces.ConfigurationService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Optional;

@Priority(100)
@Authenticated
@Decorator
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class ConfigurationAuthProxy implements ConfigurationService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    ConfigurationService configurationService;


    @Override
    public List<ConfigurationDto> listAll() {
        return configurationService.listAll();
    }

    @Override
    public ConfigurationDto getByName(String name) {
        return configurationService.getByName(name);
    }

    @Override
    public ConfigurationDto update(ConfigurationDto configurationDto) {
        return configurationService.update(configurationDto);
    }
}
