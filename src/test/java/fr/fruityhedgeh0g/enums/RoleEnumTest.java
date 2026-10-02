package fr.fruityhedgeh0g.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static fr.fruityhedgeh0g.enums.RoleEnum.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleEnumTest {

    @ParameterizedTest(name = "{0} sets {1} → {2}: {3}")
    @CsvSource({
            // The Bureau grants and revokes membre and chef_de_groupe
            "BUREAU, BENEVOLE, MEMBRE, true",
            "BUREAU, MEMBRE, CHEF_DE_GROUPE, true",
            "BUREAU, CHEF_DE_GROUPE, MEMBRE, true",
            "BUREAU, MEMBRE, BENEVOLE, true",
            "BUREAU, MEMBRE, BUREAU, false",
            "BUREAU, BUREAU, MEMBRE, false",
            "BUREAU, MEMBRE, ADMIN, false",
            // An Admin grants and revokes bureau
            "ADMIN, MEMBRE, BUREAU, true",
            "ADMIN, BUREAU, MEMBRE, true",
            "ADMIN, BENEVOLE, MEMBRE, true",
            "ADMIN, BUREAU, ADMIN, false",
            "ADMIN, ADMIN, BUREAU, false",
            // The Super admin grants and revokes admin
            "SUPER_ADMIN, BUREAU, ADMIN, true",
            "SUPER_ADMIN, ADMIN, BUREAU, true",
            "SUPER_ADMIN, ADMIN, SUPER_ADMIN, false",
            "SUPER_ADMIN, SUPER_ADMIN, ADMIN, false",
            // Below the Bureau, nobody promotes
            "CHEF_DE_GROUPE, BENEVOLE, MEMBRE, false",
            "MEMBRE, BENEVOLE, MEMBRE, false",
            // A registered person never goes back to visiteur
            "BUREAU, BENEVOLE, VISITEUR, false",
            "SUPER_ADMIN, MEMBRE, VISITEUR, false",
            // A visiteur is not registered: there is no Role to change
            "BUREAU, VISITEUR, MEMBRE, false",
    })
    void promotionChain(String actor, String current, String next, boolean allowed) {
        assertEquals(allowed, valueOf(actor).maySetRole(valueOf(current), valueOf(next)));
    }
}
