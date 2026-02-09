package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.entities.medias.MediaEntity;
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

    @Transactional
    public Try<List<MediaDto>> getAllMedia(){
        return null;
    }

    @Transactional
    public Try<MediaDto> getMediaById( UUID mediaId){
        return null;
    }

    @Transactional
    public Try<MediaDto> createMedia( MediaDto mediaDto){
        return null;
    }

    //TODO : Développer l'update
    @Transactional
    public Try<MediaDto> updateMedia( MediaDto mediaDto){
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    @Transactional
    public Try<Void> deleteMedia( UUID mediaId) {
        return null;
    }
}
