package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.ConfigurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.entities.configurations.ConfigurationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface ConfigurationMapper {

    ConfigurationDto toDto(ConfigurationEntity entity);

    ConfigurationEntity toEntity(ConfigurationDto dto);
}
