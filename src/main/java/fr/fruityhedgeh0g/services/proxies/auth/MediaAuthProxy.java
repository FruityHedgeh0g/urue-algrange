package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.services.interfaces.MediaService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@Priority(100)
@Authenticated
@Decorator
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class MediaAuthProxy implements MediaService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    MediaService mediaService;


    @Override
    public List<MediaDto> listAll() {
        return mediaService.listAll();
    }

    @Override
    public MediaDto getById(UUID mediaId) {
        return mediaService.getById(mediaId);
    }

    @Override
    public MediaDto create(MediaDto mediaDto) {
        return mediaService.create(mediaDto);
    }

    @Override
    public MediaDto update(MediaDto mediaDto) {
        return mediaService.update(mediaDto);
    }

    @Override
    public void delete(UUID mediaId) {
        mediaService.delete(mediaId);
    }
}
