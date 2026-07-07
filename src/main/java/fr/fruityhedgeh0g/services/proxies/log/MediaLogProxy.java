package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.MediaService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@Priority(200)
@Decorator
public class MediaLogProxy implements MediaService {

    @Inject
    @Delegate
    MediaService mediaService;

    @Override
    public List<MediaDto> listAll() {
        Log.debugf("Retrieving all medias...");
        return Try.of(mediaService::listAll)
                .onSuccess(medias -> Log.debugf("%d medias retrieved.",medias.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving medias."))
                .get();
    }

    @Override
    public MediaDto getById(UUID mediaId) {
        Log.debugf("Retrieving media by id %s...",mediaId);
        return Try.of(() -> mediaService.getById(mediaId))
                .onSuccess(media -> {
                        Log.debugf("Media retrieved: "+media.toString());
                })
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"Media with id %s not found.", mediaId);
                        default -> Log.errorf(t,"An error occurred while retrieving media.");
                    }
                })
                .get();
    }

    @Override
    public MediaDto create(MediaDto mediaDto) {
        return null;
    }

    @Override
    public MediaDto update(MediaDto mediaDto) {
        return null;
    }

    @Override
    public void delete(UUID mediaId) {

    }
}
