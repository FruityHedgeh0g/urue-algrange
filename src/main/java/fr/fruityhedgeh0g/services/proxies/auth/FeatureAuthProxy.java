package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.services.interfaces.FeatureService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Priority(100)
@Authenticated
@Decorator
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class FeatureAuthProxy implements FeatureService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    FeatureService featureService;


    @Override
    public List<FeatureDto> listAll() {
        return featureService.listAll();
    }

    @Override
    public FeatureDto getByName(String name) {
        return featureService.getByName(name);
    }

    @Override
    public FeatureDto update(FeatureDto featureDto) {
        return featureService.update(featureDto);
    }
}
