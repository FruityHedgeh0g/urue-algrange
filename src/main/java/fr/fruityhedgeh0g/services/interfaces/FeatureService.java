package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicFeatureService;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeatureService extends PublicFeatureService {

//    Try<List<FeatureDto>> getAllFeatures();
//    Try<FeatureDto> getFeatureByName(@NotBlank String name);
//    Try<FeatureDto> updateFeature(@NotNull @Valid FeatureDto featureDto);
}
