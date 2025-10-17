package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.SerieDto;
import fr.fruityhedgeh0g.model.entities.SerieEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface SerieMapper {
    SerieDto toDto(SerieEntity entity);

    SerieEntity toEntity(SerieDto dto);
}
