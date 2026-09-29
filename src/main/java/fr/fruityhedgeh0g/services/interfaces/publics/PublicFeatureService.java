package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface PublicFeatureService {
    List<FeatureDto> listAll();
    FeatureDto getByName(@NotNull String name);
    FeatureDto update(@NotNull @Valid FeatureDto featureDto );

//    Try<List<FeatureDto>> getAllFeatures();
//    Try<FeatureDto> getFeatureByName(@NotBlank String name);
//    Try<FeatureDto> updateFeature(@NotNull @Valid FeatureDto featureDto);
}
