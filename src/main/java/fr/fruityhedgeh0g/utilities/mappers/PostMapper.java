package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.PostDtos.NestedPostDto;
import fr.fruityhedgeh0g.dtos.PostDtos.PostDto;
import fr.fruityhedgeh0g.entities.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta-cdi", uses = MediaMapper.class)
public interface PostMapper {
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "banner", ignore = true)
    PostEntity toEntity(PostDto dto);

    PostDto toDto(PostEntity entity);

    NestedPostDto toNestedDto(PostEntity entity);
}
