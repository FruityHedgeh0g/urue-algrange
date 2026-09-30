package fr.fruityhedgeh0g.services.interfaces.internals;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface InternalEventService {
    /** On closing a Secteur: unfinished Events become Annulé, one En cours becomes Archivé; sign-ups are kept. */
    void doCloseEventsOfSector(@NotNull UUID sectorId);
}
