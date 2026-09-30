package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.enums.RoleEnum;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.repositories.UserRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

/** Test people from Membre up belong to the test's Secteur (ADR 0004); the Super admin to none. */
final class SecteurFixtures {

    static final Set<RoleEnum> WITH_A_SECTEUR = EnumSet.of(RoleEnum.MEMBRE, RoleEnum.CHEF_DE_GROUPE, RoleEnum.BUREAU, RoleEnum.ADMIN);

    private SecteurFixtures() {
    }

    /** Attaches every person without a Secteur, from Membre up, to the Secteur. */
    static void attachToSecteur(UserRepository users, SectorRepository sectors, UUID sectorId) {
        QuarkusTransaction.requiringNew().run(() -> {
            SectorEntity sector = sectors.findById(sectorId);
            users.update("sector = ?1 where sector is null and role in ?2", sector, WITH_A_SECTEUR);
        });
    }

    /** Detaches everyone from the Secteur, so that it can be deleted. */
    static void detachFromSecteur(UserRepository users, SectorRepository sectors, UUID sectorId) {
        QuarkusTransaction.requiringNew().run(() -> {
            SectorEntity sector = sectors.findById(sectorId);
            if (sector != null) users.update("sector = null where sector = ?1", sector);
        });
    }
}
