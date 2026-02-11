package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.configurationDtos.ConfigurationDto;
import fr.fruityhedgeh0g.entities.configurations.ConfigurationEntity;
import org.mapstruct.*;

@Mapper(componentModel = "jakarta-cdi")
public interface ConfigurationMapper {

    ConfigurationDto toDto(ConfigurationEntity entity);

    ConfigurationEntity toEntity(ConfigurationDto dto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "value", target = "value")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ConfigurationEntity partialDtoToEntity(@MappingTarget ConfigurationEntity entity, ConfigurationDto dto);
}
