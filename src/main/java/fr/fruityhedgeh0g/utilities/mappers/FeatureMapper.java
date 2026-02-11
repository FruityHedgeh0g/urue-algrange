package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.featureDtos.FeatureDto;
import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "jakarta-cdi")
public interface FeatureMapper {
    FeatureDto toDto(FeatureEntity entity);

    FeatureEntity toEntity(FeatureDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FeatureEntity partialDtoToEntity(@MappingTarget FeatureEntity entity, FeatureDto dto);

}
