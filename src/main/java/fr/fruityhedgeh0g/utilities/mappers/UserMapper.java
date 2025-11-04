package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.UserDto;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "jakarta-cdi", uses = {RoleMapper.class, EventMapper.class, GroupMapper.class})
public interface UserMapper {

    @Mappings({
            @Mapping(target = "group", qualifiedByName = "GroupDtoToNestedEntity"),
            @Mapping(target = "organizedEvents", qualifiedByName = "EventDtoToNestedEntity"),
            @Mapping(target = "participatedEvents", qualifiedByName = "EventDtoToNestedEntity"),
            @Mapping(target = "createdEvents", qualifiedByName = "EventDtoToNestedEntity")
    })
    UserEntity toEntity(UserDto dto);

    @Mappings({
            @Mapping(target = "group", qualifiedByName = "GroupEntityToNestedDto"),
            @Mapping(target = "organizedEvents", qualifiedByName = "EventEntityToNestedDto"),
            @Mapping(target = "participatedEvents", qualifiedByName = "EventEntityToNestedDto"),
            @Mapping(target = "createdEvents", qualifiedByName = "EventEntityToNestedDto")
    })
    UserDto toDto(UserEntity entity);

    @Named("UserEntityToNestedDto")
    @Mappings({
        @Mapping(target = "group", ignore = true),
        @Mapping(target = "organizedEvents", ignore = true),
        @Mapping(target = "participatedEvents", ignore = true),
        @Mapping(target = "createdEvents", ignore = true)
    })
    UserDto toNestedDto(UserEntity entity);

    @Named("UserDtoToNestedEntity")
    @Mappings({
            @Mapping(target = "group", ignore = true),
            @Mapping(target = "organizedEvents", ignore = true),
            @Mapping(target = "participatedEvents", ignore = true),
            @Mapping(target = "createdEvents", ignore = true)
    })
    UserEntity toNestedEntity(UserDto dto);
}
