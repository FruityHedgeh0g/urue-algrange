package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Authenticated
@Identifier("serviceProxy")
public class SectorProxy implements SectorService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
    SectorService sectorService;

    @Override
    public Try<List<SectorDto>> getAllSectors() {
        return sectorService.getAllSectors();
    }

    @Override
    public Try<SectorDto> getSectorById(UUID sectorId) {
        return sectorService.getSectorById(sectorId);
    }

    @Override
    public Try<SectorDto> createSector(SectorDto sectorDto) {
        return sectorService.createSector(sectorDto);
    }

    @Override
    public Try<SectorDto> updateSector(SectorDto sectorDto) {
        return sectorService.updateSector(sectorDto);
    }

    @Override
    public Try<Void> deleteSector(UUID sectorId) {
        return sectorService.deleteSector(sectorId);
    }

    @Override
    public Try<SectorDto> assignGroupToSector(UUID sectorId, UUID groupId) {
        return sectorService.assignGroupToSector(sectorId, groupId);
    }

    @Override
    public Try<SectorDto> unassignGroupFromSector(UUID sectorId, UUID groupId) {
        return sectorService.unassignGroupFromSector(sectorId, groupId);
    }
}
