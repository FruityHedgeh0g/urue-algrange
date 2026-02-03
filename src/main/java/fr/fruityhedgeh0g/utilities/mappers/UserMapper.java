package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.UserDtos.NestedUserDto;
import fr.fruityhedgeh0g.dtos.UserDtos.UserDto;
import fr.fruityhedgeh0g.entities.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "jakarta-cdi", uses = {RoleMapper.class, EventMapper.class, GroupMapper.class})
public interface UserMapper {

    UserEntity toEntity(UserDto dto);

    UserDto toDto(UserEntity entity);

    NestedUserDto toNestedDto(UserEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserEntity partialDtoToEntity(@MappingTarget UserEntity userEntity, UserDto userDto);
//    @Mappings({
//            @Mapping(target = "group", qualifiedByName = "GroupDtoToNestedEntity"),
//            @Mapping(target = "organizedEvents", qualifiedByName = "EventDtoToNestedEntity"),
//            @Mapping(target = "participatedEvents", qualifiedByName = "EventDtoToNestedEntity"),
//            @Mapping(target = "createdEvents", qualifiedByName = "EventDtoToNestedEntity")
//    })


//    @Mappings({
//            @Mapping(target = "group", qualifiedByName = "GroupEntityToNestedDto"),
//            @Mapping(target = "organizedEvents", qualifiedByName = "EventEntityToNestedDto"),
//            @Mapping(target = "participatedEvents", qualifiedByName = "EventEntityToNestedDto"),
//            @Mapping(target = "createdEvents", qualifiedByName = "EventEntityToNestedDto")
//    })




//    @Named("UserEntityToNestedDto")
//    @Mappings({
//        @Mapping(target = "group", ignore = true),
//        @Mapping(target = "organizedEvents", ignore = true),
//        @Mapping(target = "participatedEvents", ignore = true),
//        @Mapping(target = "createdEvents", ignore = true)
//    })
//    UserDto toNestedDto(UserEntity entity);
//
//    @Named("UserDtoToNestedEntity")
//    @Mappings({
//            @Mapping(target = "group", ignore = true),
//            @Mapping(target = "organizedEvents", ignore = true),
//            @Mapping(target = "participatedEvents", ignore = true),
//            @Mapping(target = "createdEvents", ignore = true)
//    })
//    UserEntity toNestedEntity(UserDto dto);
//
//    @Named("UserDtoSetToNestedEntitySet")
//    default Set<UserEntity> toNestedEntitySet(Set<UserDto> dtos){
//        return dtos.stream().map(this::toNestedEntity).collect(Collectors.toSet());
//    };
//
//    @Named("UserEntitySetToNestedDtoSet")
//    default Set<UserDto> toNestedDtoSet(Set<UserEntity> entities){
//        return entities.stream().map(this::toNestedDto).collect(Collectors.toSet());
//    };
//
}
