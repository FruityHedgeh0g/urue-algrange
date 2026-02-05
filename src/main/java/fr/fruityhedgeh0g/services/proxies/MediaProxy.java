package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.services.interfaces.MediaService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@Identifier("mediaProxy")
@AllArgsConstructor
@ApplicationScoped
@Authenticated
public class MediaProxy implements MediaService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    MediaService mediaService;

    @Override
    public Try<List<MediaDto>> getAllMedia() {
        return null;
    }

    @Override
    public Try<MediaDto> getMediaById(UUID mediaId) {
        return null;
    }

    @Override
    public Try<MediaDto> createMedia(MediaDto mediaDto) {
        return null;
    }

    @Override
    public Try<MediaDto> updateMedia(MediaDto mediaDto) {
        return null;
    }

    @Override
    public void deleteMedia(UUID mediaId) {

    }
}
