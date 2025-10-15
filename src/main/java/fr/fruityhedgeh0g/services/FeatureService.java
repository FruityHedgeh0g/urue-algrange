package fr.fruityhedgeh0g.services;

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

import java.util.NoSuchElementException;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
public class FeatureService {

    @Inject
    FeatureRepository featureRepository;

    @Inject
    FeatureMapper featureMapper;

    public FeatureDto getFeatureByName(@NotBlank String name) {
        Log.info("Getting feature by name: " + name);
        return Try.of(() -> featureRepository.findByName(name)
                        .orElseThrow(() -> new NoSuchElementException("Feature "+name+" not found")))
                .onFailure(e -> Log.error("Error getting feature by name: " + name, e))
                .map(featureMapper::toDto)
                .get();
    }
}
