package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.services.interfaces.MediaService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Alternative
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
}
