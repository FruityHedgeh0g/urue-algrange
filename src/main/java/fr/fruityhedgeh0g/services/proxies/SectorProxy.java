package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
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

@Identifier("sectorProxy")
@AllArgsConstructor
@ApplicationScoped
@Authenticated
public class SectorProxy implements SectorService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    SectorService sectorService;

    @Override
    public Try<List<SectorDto>> getAllSectors() {
        return null;
    }

    @Override
    public Try<SectorDto> getSectorById(UUID sectorId) {
        return null;
    }

    @Override
    public Try<SectorDto> createSector(SectorDto sectorDto) {
        return null;
    }

    @Override
    public Try<SectorDto> updateSector(SectorDto sectorDto) {
        return null;
    }

    @Override
    public void deleteSector(UUID sectorId) {

    }

    @Override
    public Try<SectorDto> assignGroupToSector(UUID sectorId, UUID groupId) {
        return null;
    }

    @Override
    public Try<SectorDto> unassignGroupFromSector(UUID sectorId, UUID groupId) {
        return null;
    }
}
