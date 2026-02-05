package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.services.interfaces.FeatureService;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;

@Identifier("featureProxy")
@AllArgsConstructor
@ApplicationScoped
public class FeatureProxy implements FeatureService {
    @Inject
    FeatureService featureService;

    @Override
    public Try<List<FeatureEntity>> getAllFeatures() {
        return null;
    }

    @Override
    public Try<FeatureEntity> getFeatureByName(String name) {
        return null;
    }

    @Override
    public Try<FeatureEntity> updateFeature(FeatureEntity featureEntity) {
        return null;
    }
}
