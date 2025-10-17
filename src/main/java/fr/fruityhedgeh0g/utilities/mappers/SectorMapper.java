package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.SectorDto;
import fr.fruityhedgeh0g.model.entities.SectorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface SectorMapper {

    SectorEntity toDto(SectorDto dto);

    SectorDto toDto(SectorEntity entity);
}
