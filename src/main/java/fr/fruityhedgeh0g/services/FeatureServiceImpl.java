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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class FeatureServiceImpl implements FeatureService {

    @Inject
    FeatureRepository featureRepository;

    @Override
    @Transactional
    public Try<FeatureEntity> getFeatureByName( String name) {
        Log.infof("Getting feature by name: %s", name);
        return Try.of(() -> featureRepository
                .findByName(name)
                .orElseThrow(() ->
                        new UnknownResourceException("Feature not found: " + name))
        ).onFailure(e -> {
            if (e instanceof UnknownResourceException ex) {
                Log.warn(ex.getMessage());
            } else {
                Log.errorf(e,"Error getting feature by name: %s", name );
            }
        });


    }

    @Override
    @Transactional
    public Try<List<FeatureEntity>> getAllFeatures() {
        Log.info("Getting all features");
        return Try.of(() -> featureRepository
                .listAll()
                .stream()
                .toList())
                .onFailure(e -> {
                    Log.errorf(e,"Error getting all features");
                });
    }

    //TODO : Développer l'update
    public Try<FeatureEntity> updateFeature( FeatureDto dto) {
        return null;
    }
}
