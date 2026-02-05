package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.sectorDtos.NestedSectorDto;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.SectorEntity;
import org.mapstruct.*;

@Mapper(componentModel = "jakarta-cdi",uses = GroupMapper.class)
public interface SectorMapper {

//    @Mapping(target = "groups", qualifiedByName = "GroupDtoToNestedEntity")
    SectorEntity toEntity(SectorDto dto);

//    @Mapping(target = "groups", qualifiedByName = "GroupEntityToNestedDto")
    SectorDto toDto(SectorEntity entity);

    NestedSectorDto toNestedDto(SectorEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SectorEntity partialDtoToEntity(@MappingTarget SectorEntity sectorEntity, SectorDto sectorDto);

//    @Named("SectorDtoToNestedEntity")
//    @Mapping(target = "groups", ignore = true)
//    SectorEntity toNestedEntity(SectorDto dto);
//
//    @Named("SectorEntityToNestedDto")
//    @Mapping(target = "groups", ignore = true)
//    SectorDto toNestedDto(SectorEntity entity);
//

}
