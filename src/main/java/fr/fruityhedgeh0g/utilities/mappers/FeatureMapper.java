package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.FeatureDtos.FeatureDto;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface FeatureMapper {
    FeatureDto toDto(FeatureEntity entity);

    FeatureEntity toEntity(FeatureDto dto);

}
