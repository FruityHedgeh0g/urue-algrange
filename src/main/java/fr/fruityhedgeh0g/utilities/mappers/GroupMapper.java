package fr.fruityhedgeh0g.utilities.mappers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.dtos.GroupDto;
import fr.fruityhedgeh0g.model.dtos.SectorDto;
import fr.fruityhedgeh0g.model.dtos.UserDto;
import fr.fruityhedgeh0g.model.entities.GroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "jakarta-cdi", uses = {UserMapper.class,SectorMapper.class})
public interface GroupMapper {

    @Mappings({
            @Mapping(target = "members" ,qualifiedByName = "UserEntityToNestedDto"),
            @Mapping(target = "sector", qualifiedByName = "SectorEntityToNestedDto")
    })
    GroupDto toDto(GroupEntity entity);

    @Mappings({
            @Mapping(target = "members", qualifiedByName = "UserDtoToNestedEntity"),
            @Mapping(target = "sector", qualifiedByName = "SectorDtoToNestedEntity")
    })
    GroupEntity toEntity(GroupDto dto);

    @Named("GroupDtoToNestedEntity")
    @Mappings({
            @Mapping(target = "members", ignore = true),
            @Mapping(target = "sector", ignore = true)
    })
    GroupEntity toNestedEntity(GroupDto dto);

    @Named("GroupEntityToNestedDto")
    @Mappings({
            @Mapping(target = "members", ignore = true),
            @Mapping(target = "sector", ignore = true)
    })
    GroupDto toNestedDto(GroupEntity entity);
}
