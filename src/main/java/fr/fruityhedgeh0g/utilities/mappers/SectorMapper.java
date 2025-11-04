package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.SectorDto;
import fr.fruityhedgeh0g.model.entities.SectorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "jakarta-cdi",uses = GroupMapper.class)
public interface SectorMapper {

    @Mapping(target = "groups", qualifiedByName = "GroupDtoToNestedEntity")
    SectorEntity toEntity(SectorDto dto);

    @Mapping(target = "groups", qualifiedByName = "GroupEntityToNestedDto")
    SectorDto toDto(SectorEntity entity);

    @Named("SectorDtoToNestedEntity")
    @Mapping(target = "groups", ignore = true)
    SectorEntity toNestedEntity(SectorDto dto);

    @Named("SectorEntityToNestedDto")
    @Mapping(target = "groups", ignore = true)
    SectorDto toNestedDto(SectorEntity entity);
}
