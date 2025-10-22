package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.SeriesDto;
import fr.fruityhedgeh0g.model.entities.SeriesEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface SeriesMapper {
    SeriesDto toDto(SeriesEntity entity);

    SeriesEntity toEntity(SeriesDto dto);
}
