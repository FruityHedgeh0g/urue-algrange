package fr.fruityhedgeh0g.security;

import fr.fruityhedgeh0g.entities.SectorEntity;

import java.util.UUID;

/**
 * The Secteurs a person manages (ADR 0004): every Secteur for the Super admin,
 * their own for anyone else, none for someone without a Secteur.
 */
public record SecteurScope(boolean everySecteur, UUID sectorId) {

    public static final SecteurScope EVERY_SECTEUR = new SecteurScope(true, null);

    public static SecteurScope of(SectorEntity sector) {
        return new SecteurScope(false, sector == null ? null : sector.getSectorId());
    }

    public boolean covers(UUID otherSectorId) {
        return everySecteur || (sectorId != null && sectorId.equals(otherSectorId));
    }

    public boolean covers(SectorEntity sector) {
        return everySecteur || (sector != null && covers(sector.getSectorId()));
    }
}
