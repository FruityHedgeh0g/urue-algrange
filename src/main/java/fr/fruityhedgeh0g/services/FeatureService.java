package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.FeatureRepository;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.PackagePrivate;

import java.util.List;

import static io.smallrye.mutiny.helpers.spies.Spy.onFailure;

@ApplicationScoped
@AllArgsConstructor
public class FeatureService {

    @Inject
    FeatureRepository featureRepository;

    @PackagePrivate
    Try<FeatureEntity> getFeatureByName(@NotBlank String name) {
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

    @PackagePrivate
    Try<List<FeatureEntity>> getAllFeatures() {
        Log.debug("Getting all features");
        return Try.of(() -> featureRepository.findAll()
                .stream()
                .toList())
                .onFailure(e -> Log.error("Error getting all features", e));
    }

    @PackagePrivate
    //TODO : Développer l'update
    Try<FeatureEntity> updateFeature(@NonNull FeatureEntity entity) {
        return null;
    }
}
