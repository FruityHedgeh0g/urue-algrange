package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.configurations.FeatureDto;
import fr.fruityhedgeh0g.model.entities.configurations.FeatureEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface FeatureMapper {
    FeatureDto toDto(FeatureEntity entity);

    FeatureEntity toEntity(FeatureDto dto);

}
