package fr.fruityhedgeh0g.services.proxies.auth;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
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
public class SectorAuthProxy implements SectorService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Delegate
    SectorService sectorService;


    @Override
    public List<SectorDto> listAll() {
        return sectorService.listAll();
    }

    @Override
    public SectorDto getById(UUID sectorId) {
        return sectorService.getById(sectorId);
    }

    @Override
    public SectorDto create(SectorDto sectorDto) {
        return sectorService.create(sectorDto);
    }

    @Override
    public SectorDto update(SectorDto sectorDto) {
        return sectorService.update(sectorDto);
    }

    @Override
    public void delete(UUID sectorId) {
        sectorService.delete(sectorId);
    }

    @Override
    public void assignGroup(UUID sectorId, UUID groupId) {
        sectorService.assignGroup(sectorId,groupId);
    }

    @Override
    public void unassignGroup(UUID sectorId, UUID groupId) {
        sectorService.unassignGroup(sectorId,groupId);
    }
}
