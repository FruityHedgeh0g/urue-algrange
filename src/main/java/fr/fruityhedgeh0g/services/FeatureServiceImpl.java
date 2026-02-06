package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.FeatureRepository;
import fr.fruityhedgeh0g.services.interfaces.FeatureService;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;

import static io.smallrye.mutiny.helpers.spies.Spy.onFailure;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class FeatureServiceImpl implements FeatureService {

    @Inject
    FeatureRepository featureRepository;

    public Try<FeatureEntity> getFeatureByName( String name) {
        Log.debug("Getting feature by name: " + name);
        return Try.of(() -> featureRepository
                        .findByIdOptional(name)
                        .orElseThrow(() -> new UnknownResourceException("Feature not found: " + name)))
                .onFailure(e -> {
                    if (e instanceof UnknownResourceException) {
                        Log.warn("Feature not found: " + name);
                    }else {
                        Log.error("Error getting feature by name: " + name, e);
                    }
                });


    }

    public Try<List<FeatureEntity>> getAllFeatures() {
        Log.debug("Getting all features");
        return Try.of(() -> featureRepository.findAll()
                .stream()
                .toList())
                .onFailure(e -> Log.error("Error getting all features", e));
    }

    //TODO : Développer l'update
    public Try<FeatureEntity> updateFeature( FeatureEntity entity) {
        return null;
    }
}
