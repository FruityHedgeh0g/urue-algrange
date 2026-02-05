package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.eventDtos.EventDto;
import fr.fruityhedgeh0g.entities.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta-cdi", uses = UserMapper.class)
public interface EventMapper {

//    @Mappings({
//            @Mapping(target = "creator", qualifiedByName = "UserDtoToNestedEntity"),
//            @Mapping(target = "organizers", qualifiedByName = "UserDtoSetToNestedEntitySet"),
//            @Mapping(target = "participants", qualifiedByName = "UserDtoSetToNestedEntitySet")
//    })

    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "organizers", ignore = true)
    EventEntity toEntity(EventDto dto);

//    @Mappings({
//            @Mapping(target = "creator", qualifiedByName = "UserEntityToNestedDto"),
//            @Mapping(target = "organizers", qualifiedByName = "UserEntitySetToNestedDtoSet"),
//            @Mapping(target = "participants", qualifiedByName = "UserEntitySetToNestedDtoSet")
//    })
    EventDto toDto(EventEntity entity);


//    @Named("EventDtoToNestedEntity")
//    @Mappings({
//            @Mapping(target = "creator", ignore = true),
//            @Mapping(target = "organizers", ignore = true),
//            @Mapping(target = "participants", ignore = true)
//    })
//    EventEntity toNestedEntity(EventDto dto);
//
//    @Named("EventEntityToNestedDto")
//    @Mappings({
//            @Mapping(target = "creator", ignore = true),
//            @Mapping(target = "organizers", ignore = true),
//            @Mapping(target = "participants", ignore = true)
//    })
//    EventDto toNestedDto(EventEntity entity);

}
