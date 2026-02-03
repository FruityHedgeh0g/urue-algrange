package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.PostDtos.NestedPostDto;
import fr.fruityhedgeh0g.dtos.PostDtos.PostDto;
import fr.fruityhedgeh0g.entities.PostEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi", uses = MediaMapper.class)
public interface PostMapper {
    PostEntity toEntity(PostDto dto);

    PostDto toDto(PostEntity entity);

    NestedPostDto toNestedDto(PostEntity entity);
}
