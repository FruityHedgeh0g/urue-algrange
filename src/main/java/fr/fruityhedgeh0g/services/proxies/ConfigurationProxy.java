package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.services.interfaces.ConfigurationService;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@AllArgsConstructor
@ApplicationScoped
@Authenticated
@Identifier("serviceProxy")
public class ConfigurationProxy implements ConfigurationService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
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
