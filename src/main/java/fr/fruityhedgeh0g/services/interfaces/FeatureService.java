package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface FeatureService {
    Try<List<FeatureDto>> listAll();
    Try<FeatureDto> getByName(@NotNull String name);
    Try<FeatureDto> update(@NotNull @Valid FeatureDto featureDto );

//    Try<List<FeatureDto>> getAllFeatures();
//    Try<FeatureDto> getFeatureByName(@NotBlank String name);
//    Try<FeatureDto> updateFeature(@NotNull @Valid FeatureDto featureDto);
}
