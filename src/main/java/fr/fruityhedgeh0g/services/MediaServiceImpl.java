package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.entities.medias.MediaEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.MediaRepository;
import fr.fruityhedgeh0g.services.interfaces.MediaService;
import fr.fruityhedgeh0g.utilities.mappers.MediaMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class MediaServiceImpl implements MediaService {
    @Inject
    MediaRepository mediaRepository;

    @Inject
    MediaMapper mediaMapper;

    @Override
    public Try<List<MediaDto>> listAll() {
        return null;
    }

    @Override
    public Try<MediaDto> getById(UUID mediaId) {
        return null;
    }

    @Override
    public Try<MediaDto> create(MediaDto mediaDto) {
        return null;
    }

    @Override
    public Try<MediaDto> update(MediaDto mediaDto) {
        return null;
    }

    @Override
    public Try<MediaDto> delete(UUID mediaId) {
        return null;
    }

//    @Transactional
//    public Try<List<MediaDto>> getAllMedia(){
//        Log.info("Getting all media");
//        return Try.of(() -> mediaRepository
//                .findAll()
//                .stream()
//                .map(mediaMapper::toDto)
//                .toList())
//                .onFailure(e ->
//                        Log.error("Error getting all media", e)
//                );
//    }
//
//    @Override
//    @Transactional
//    public Try<MediaDto> getMediaById( UUID mediaId){
//        Log.infof("Getting media with id: %s", mediaId);
//        return Try.of(() -> mediaRepository
//                .findByIdOptional(mediaId)
//                .orElseThrow(() ->
//                        new UnknownResourceException("Media not found: " + mediaId)))
//                .map(mediaMapper::toDto)
//                .onFailure(e -> {
//                    if (e instanceof UnknownResourceException ex) {
//                        Log.warn(ex.getMessage());
//                    } else {
//                        Log.errorf(e, "Error getting media with id: %s", mediaId);
//                    }
//                });
//
//    }
//
//    @Override
//    @Transactional
//    public Try<MediaDto> createMedia( MediaDto mediaDto){
//        return null;
//    }
//
//    //TODO : Développer l'update
//    @Override
//    @Transactional
//    public Try<MediaDto> updateMedia( MediaDto mediaDto){
//        return null;
//    }
//
//    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
//    @Override
//    @Transactional
//    public Try<Void> deleteMedia( UUID mediaId) {
//        return null;
//    }
}
