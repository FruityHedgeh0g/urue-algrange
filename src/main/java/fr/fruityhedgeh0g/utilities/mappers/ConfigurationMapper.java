package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.configurations.ConfigurationDto;
import fr.fruityhedgeh0g.model.entities.configurations.ConfigurationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface ConfigurationMapper {

    ConfigurationDto toDto(ConfigurationEntity entity);

    ConfigurationEntity toEntity(ConfigurationDto dto);
}
