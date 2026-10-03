package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.entities.medias.MediaContentEntity;
import fr.fruityhedgeh0g.entities.medias.MediaEntity;
import fr.fruityhedgeh0g.entities.medias.PhotoEntity;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.MediaContentRepository;
import fr.fruityhedgeh0g.repositories.MediaRepository;
import fr.fruityhedgeh0g.utilities.mappers.MediaMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The files behind the medias: uploaded by the Bureau, kept in the database (ADR 0008) and served to everyone.
 * Images only: a video does not belong in the database, and an SVG could carry a script.
 */
@ApplicationScoped
public class MediaFileService {

    static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    public record MediaFile(String mimeType, byte[] content) {}

    @Inject MediaRepository mediaRepository;
    @Inject MediaContentRepository contentRepository;
    @Inject MediaMapper mediaMapper;

    @Transactional
    public MediaDto upload(String filename, String mimeType, byte[] content, String alt) {
        if (mimeType == null || !IMAGE_TYPES.contains(mimeType))
            throw new InvalidResourceException("Only JPEG, PNG, WebP and GIF images are accepted: " + mimeType);
        if (content.length == 0 || content.length > MediaContentEntity.MAX_SIZE)
            throw new InvalidResourceException("An image weighs at most 8 MB: " + content.length + " bytes");

        PhotoEntity photo = new PhotoEntity();
        photo.setOriginalFilename(filename == null || filename.isBlank() ? "image" : filename);
        photo.setFileKey("database");
        // The discriminator column, required by validation though Hibernate writes it from the class
        photo.setContentType("PHOTO");
        photo.setMimeType(mimeType);
        photo.setFileSize(content.length);
        photo.setAlt(alt == null || alt.isBlank() ? null : alt.trim());
        mediaRepository.persist(photo);
        mediaRepository.flush();
        contentRepository.persist(new MediaContentEntity(photo.getMediaId(), content));
        return mediaMapper.toDto((MediaEntity) photo);
    }

    @Transactional
    public MediaDto describe(UUID mediaId, String alt) {
        MediaEntity media = mediaRepository.findByIdOptional(mediaId)
                .orElseThrow(() -> new UnknownResourceException("Media not found: " + mediaId));
        media.setAlt(alt == null || alt.isBlank() ? null : alt.trim());
        return mediaMapper.toDto(media);
    }

    @Transactional
    public Optional<MediaFile> file(UUID mediaId) {
        return mediaRepository.findByIdOptional(mediaId).flatMap(media -> contentRepository.findByIdOptional(mediaId)
                .map(content -> new MediaFile(media.getMimeType(), content.getContent())));
    }
}
