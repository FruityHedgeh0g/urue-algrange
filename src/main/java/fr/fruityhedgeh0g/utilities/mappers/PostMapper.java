package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.PostDto;
import fr.fruityhedgeh0g.model.entities.PostEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi", uses = MediaMapper.class)
public interface PostMapper {
    PostEntity toEntity(PostDto dto);

    PostDto toDto(PostEntity entity);
}
