package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.services.interfaces.FeatureService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@AllArgsConstructor
@ApplicationScoped
@Alternative
@Priority(1)
@Authenticated
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class FeatureProxy implements FeatureService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
    FeatureService featureService;

    @Override
    public Try<List<FeatureDto>> getAllFeatures() {
        return featureService.getAllFeatures();
    }

    @Override
    public Try<FeatureDto> getFeatureByName(String name) {
        return featureService.getFeatureByName(name);
    }

    @Override
    public Try<FeatureDto> updateFeature(FeatureDto featureDto) {
        return featureService.updateFeature(featureDto);
    }
}
