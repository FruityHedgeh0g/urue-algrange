package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
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
public class SectorAuthProxy implements SectorService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    SectorService sectorService;


    @Override
    public Try<List<SectorDto>> listAll() {
        return null;
    }

    @Override
    public Try<SectorDto> getById(UUID sectorId) {
        return null;
    }

    @Override
    public Try<SectorDto> create(SectorDto sectorDto) {
        return null;
    }

    @Override
    public Try<SectorDto> update(SectorDto sectorDto) {
        return null;
    }

    @Override
    public Try<SectorDto> delete(UUID sectorId) {
        return null;
    }
}
