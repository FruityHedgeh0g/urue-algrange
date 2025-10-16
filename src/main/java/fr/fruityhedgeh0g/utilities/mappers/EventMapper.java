package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.EventDto;
import fr.fruityhedgeh0g.model.entities.EventEntity;
import jakarta.inject.Inject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta-cdi", uses = UserMapper.class)
public interface EventMapper {

    EventEntity toEntity(EventDto dto);

    EventDto toDto(EventEntity entity);
}
