package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.model.dtos.configurations.FeatureDto;
import fr.fruityhedgeh0g.repositories.FeatureRepository;
import fr.fruityhedgeh0g.utilities.mappers.FeatureMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;

import static io.smallrye.mutiny.helpers.spies.Spy.onFailure;

@ApplicationScoped
@AllArgsConstructor
public class FeatureService {

    @Inject
    FeatureRepository featureRepository;

    @Inject
    FeatureMapper featureMapper;

    public Try<FeatureDto> getFeatureByName(@NotBlank String name) {
        Log.debug("Getting feature by name: " + name);
        return Try.of(() -> featureRepository
                        .findByIdOptional(name)
                        .orElseThrow(() -> new UnknownResourceException("Feature not found: " + name)))
                .map(featureMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof UnknownResourceException) {
                        Log.warn("Feature not found: " + name);
                    }else {
                        Log.error("Error getting feature by name: " + name, e);
                    }
                });


    }

    public Try<List<FeatureDto>> getAllFeatures() {
        Log.debug("Getting all features");
        return Try.of(() -> featureRepository.findAll()
                .stream()
                .map(featureMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all features", e));
    }

    //TODO : Développer l'update
    public Try<FeatureDto> updateFeature(@NonNull FeatureDto dto) {
        return null;
    }
}
