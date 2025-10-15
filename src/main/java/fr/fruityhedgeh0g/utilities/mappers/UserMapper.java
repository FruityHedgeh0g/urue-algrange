package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.EventDto;
import fr.fruityhedgeh0g.model.dtos.UserDto;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "cdi")
public interface UserMapper {

    @Mapping(target = "organizedEvents", ignore = true)
    @Mapping(target = "participatedEvents", ignore = true)
    @Mapping(target = "createdEvents", ignore = true)
    UserEntity toEntity(UserDto dto);

    @Mapping(target = "organizedEvents", ignore = true)
    @Mapping(target = "participatedEvents", ignore = true)
    @Mapping(target = "createdEvents", ignore = true)
    UserDto toDto(UserEntity entity);

}
