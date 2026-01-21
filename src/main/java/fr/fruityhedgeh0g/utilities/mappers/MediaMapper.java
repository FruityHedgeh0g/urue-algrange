package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.MediaDto;
import fr.fruityhedgeh0g.dtos.RoleDto;
import fr.fruityhedgeh0g.entities.medias.MediaEntity;
import fr.fruityhedgeh0g.entities.medias.PhotoEntity;
import fr.fruityhedgeh0g.entities.medias.VideoEntity;
import fr.fruityhedgeh0g.entities.roles.LegalRoleEntity;
import fr.fruityhedgeh0g.entities.roles.OrganizationalRoleEntity;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "jakarta-cdi")
public interface MediaMapper {

    @Mapping(target = "contentType", constant = "PHOTO")
    MediaDto toDto(PhotoEntity entity);

    @Mapping(target = "contentType", constant = "VIDEO")
    MediaDto toDto(VideoEntity entity);

    default MediaDto toDto(MediaEntity entity) {
        switch (entity) {
            case VideoEntity videoEntity -> {return toDto(videoEntity);}
            case PhotoEntity photoEntity -> {return toDto(photoEntity);}
            default -> throw new IllegalStateException("Unexpected media entity type: " + entity.getClass().getSimpleName());
        }
    }

    VideoEntity toVideoEntity(MediaDto dto);

    PhotoEntity toPhotoEntity(MediaDto dto);

    default MediaEntity toEntity(MediaDto dto) {
        switch(dto.getContentType()){
            case "VIDEO" -> {return toVideoEntity(dto);}
            case "PHOTO" -> {return toPhotoEntity(dto);}
            default -> throw new IllegalStateException("Unexpected content type: " + dto.getContentType());
        }
    }

//    default MediaDto toDto(MediaEntity entity) {
//        MediaDto dto;
//        if (entity instanceof VideoEntity){
//            dto = toDto((VideoEntity) entity);
//            //dto.setMediaType("VIDEO");
//            return dto;
//        } else if (entity instanceof PhotoEntity){
//            dto = toDto((PhotoEntity) entity);
//            //dto.setMediaType("PHOTO");
//            return dto;
//        } else throw new IllegalStateException("Unexpected value: " + entity.getClass());
//    }

//    @Named("mediaDtoToVideoEntity")
//    VideoEntity toVideoEntity(MediaDto dto);
//
//    @Named("mediaDtoToPhotoEntity")
//    PhotoEntity toPhotoEntity(MediaDto dto);
//
//    default MediaEntity toEntity(MediaDto dto){
//        return switch (dto.getClass().getSimpleName()){
//            case "VideoDto" -> toVideoEntity(dto);
//            case "PhotoDto" -> toPhotoEntity(dto);
//            default -> throw new IllegalStateException("Unexpected value: " + dto.getMediaType());
//        };
//    }
}
