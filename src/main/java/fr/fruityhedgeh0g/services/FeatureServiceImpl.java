package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.FeatureRepository;
import fr.fruityhedgeh0g.services.interfaces.FeatureService;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class FeatureServiceImpl implements FeatureService {

    @Inject
    FeatureRepository featureRepository;

    public Try<FeatureEntity> getFeatureByName( String name) {
        return null;


    }

    public Try<List<FeatureEntity>> getAllFeatures() {
        return null;
    }

    //TODO : Développer l'update
    public Try<FeatureEntity> updateFeature( FeatureDto dto) {
        return null;
    }
}
