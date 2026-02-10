package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface FeatureService {
    Try<List<FeatureEntity>> getAllFeatures();
    Try<FeatureEntity> getFeatureByName(@NotBlank String name);
    Try<FeatureEntity> updateFeature(@NotNull @Valid FeatureDto featureDto);
}
