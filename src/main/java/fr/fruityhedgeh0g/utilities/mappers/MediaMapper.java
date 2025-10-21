package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.MediaDto;
import fr.fruityhedgeh0g.model.entities.medias.MediaEntity;
import fr.fruityhedgeh0g.model.entities.medias.PhotoEntity;
import fr.fruityhedgeh0g.model.entities.medias.VideoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "jakarta-cdi")
public interface MediaMapper {

    @Named("mediaEntityToDto")
    @Mapping(target = "mediaType", constant = "VIDEO")
    MediaDto toDto(VideoEntity entity);

    @Named("mediaEntityToDto")
    @Mapping(target = "mediaType", constant = "PHOTO")
    MediaDto toDto(PhotoEntity entity);

    default MediaDto toDto(MediaEntity entity) {
        MediaDto dto;
        if (entity instanceof VideoEntity){
            dto = toDto((VideoEntity) entity);
            //dto.setMediaType("VIDEO");
            return dto;
        } else if (entity instanceof PhotoEntity){
            dto = toDto((PhotoEntity) entity);
            //dto.setMediaType("PHOTO");
            return dto;
        } else throw new IllegalStateException("Unexpected value: " + entity.getClass());
    }

    @Named("mediaDtoToVideoEntity")
    VideoEntity toVideoEntity(MediaDto dto);

    @Named("mediaDtoToPhotoEntity")
    PhotoEntity toPhotoEntity(MediaDto dto);

    default MediaEntity toEntity(MediaDto dto){
        return switch (dto.getMediaType()){
            case "VIDEO" -> toVideoEntity(dto);
            case "PHOTO" -> toPhotoEntity(dto);
            default -> throw new IllegalStateException("Unexpected value: " + dto.getMediaType());
        };
    }
}
