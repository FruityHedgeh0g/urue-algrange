package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.groupDtos.NestedGroupDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import org.mapstruct.*;

@Mapper(componentModel = "jakarta-cdi", uses = {UserMapper.class,SectorMapper.class})
public interface GroupMapper {

    GroupDto toDto(GroupEntity entity);

    NestedGroupDto toNestedDto(GroupEntity entity);

    @Mapping(target = "chef", ignore = true)
    GroupEntity toEntity(GroupDto dto);

    @Mapping(target = "chef", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GroupEntity partialDtoToEntity(@MappingTarget GroupEntity groupEntity, GroupDto groupDto);



}
