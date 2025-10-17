package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.GroupDto;
import fr.fruityhedgeh0g.model.entities.GroupEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface GroupMapper {

    GroupDto toDto(GroupEntity entity);

    GroupEntity toEntity(GroupDto dto);
}
