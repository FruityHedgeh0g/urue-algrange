package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.EventDto;
import fr.fruityhedgeh0g.model.entities.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "jakarta-cdi", uses = UserMapper.class)
public interface EventMapper {

    @Mappings({
            @Mapping(target = "creator", qualifiedByName = "UserDtoToNestedEntity"),
            @Mapping(target = "organizers", qualifiedByName = "UserDtoToNestedEntity"),
            @Mapping(target = "participants", qualifiedByName = "UserDtoToNestedEntity")
    })
    EventEntity toEntity(EventDto dto);

    @Mappings({
            @Mapping(target = "creator", qualifiedByName = "UserEntityToNestedDto"),
            @Mapping(target = "organizers", qualifiedByName = "UserEntityToNestedDto"),
            @Mapping(target = "participants", qualifiedByName = "UserEntityToNestedDto")
    })
    EventDto toDto(EventEntity entity);

    @Named("EventDtoToNestedEntity")
    @Mappings({
            @Mapping(target = "creator", ignore = true),
            @Mapping(target = "organizers", ignore = true),
            @Mapping(target = "participants", ignore = true)
    })
    EventEntity toNestedEntity(EventDto dto);

    @Named("EventEntityToNestedDto")
    @Mappings({
            @Mapping(target = "creator", ignore = true),
            @Mapping(target = "organizers", ignore = true),
            @Mapping(target = "participants", ignore = true)
    })
    EventDto toNestedDto(EventEntity entity);
}
