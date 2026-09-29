package fr.fruityhedgeh0g.services.interfaces.internals;

import fr.fruityhedgeh0g.entities.SectorEntity;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface InternalSectorService {
    Optional<SectorEntity> doGetEntityById(@NotNull UUID sectorId);
}
