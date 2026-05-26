package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.services.interfaces.MediaService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("proxy")
@Authenticated
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class MediaProxy implements MediaService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
    MediaService mediaService;

    @Override
    public Try<List<MediaDto>> getAllMedia() {
        return mediaService.getAllMedia();
    }

    @Override
    public Try<MediaDto> getMediaById(UUID mediaId) {
        return mediaService.getMediaById(mediaId);
    }

    @Override
    public Try<MediaDto> createMedia(MediaDto mediaDto) {
        return mediaService.createMedia(mediaDto);
    }

    @Override
    public Try<MediaDto> updateMedia(MediaDto mediaDto) {
        return mediaService.updateMedia(mediaDto);
    }

    @Override
    public Try<Void> deleteMedia(UUID mediaId) {
        return mediaService.deleteMedia(mediaId);
    }
}
