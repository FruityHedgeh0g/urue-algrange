package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.exceptions.DuplicateEntityException;
import fr.fruityhedgeh0g.model.dtos.MediaDto;
import fr.fruityhedgeh0g.model.entities.medias.MediaEntity;
import fr.fruityhedgeh0g.repositories.MediaRepository;
import fr.fruityhedgeh0g.utilities.mappers.MediaMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class MediaService {
    @Inject
    MediaRepository mediaRepository;

    @Inject
    MediaMapper mediaMapper;

    public Try<List<MediaDto>> getAllMedia(){
        Log.info("Getting all medias");
        return null;
    }

    public Try<MediaDto> getMediaById(@NotNull UUID mediaId){
        Log.info("Getting media with id: " + mediaId);
        return Try.of(() -> mediaRepository
                        .findByIdOptional(mediaId)
                        .orElseThrow(NoSuchElementException::new))
                .map(mediaMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Media not found: " + mediaId);
                    }else {
                        Log.error("Error getting media with id: " + mediaId, e);
                    }
                });
    }

    public Try<MediaDto> createMedia(@NotNull MediaDto mediaDto){
        return Try.of(() -> {
            Log.debug("Creating media");
            MediaEntity mediaEntity = mediaMapper.toEntity(mediaDto);
            mediaRepository.persist(mediaEntity);

            Log.debug("Media created, retrieving up-to-date media infos: " + mediaEntity.getMediaId());
            return mediaMapper.toDto(
                    mediaRepository
                            .findByIdOptional(mediaEntity.getMediaId())
                            .orElseThrow(NoSuchElementException::new)
            );
        }).onFailure(e -> {
                Log.error("Error creating media", e);
        });
    }

    //TODO : Développer l'update
    public Try<MediaDto> updateMedia(@NotNull MediaDto mediaDto){
        Log.info("Updating media: " + mediaDto.getMediaId());
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    public void deleteMedia(@NotNull UUID mediaId){
        Log.info("Deleting media with id: " + mediaId);
        Try.of(() -> mediaRepository.deleteById(mediaId))
                .onFailure(e -> Log.error("Error deleting media with id: " + mediaId, e));
    }
}
