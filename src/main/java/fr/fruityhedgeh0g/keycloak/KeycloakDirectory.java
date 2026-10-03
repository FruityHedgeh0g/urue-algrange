package fr.fruityhedgeh0g.keycloak;

import java.util.Optional;
import java.util.UUID;

/** Looks people up in Keycloak, where they register; read-only. */
public interface KeycloakDirectory {

    record KeycloakPerson(UUID id, String firstName, String lastName) {}

    /** The person with exactly this username, if the realm has one. */
    Optional<KeycloakPerson> findByUsername(String username);
}
