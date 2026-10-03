package fr.fruityhedgeh0g.security;

import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.enums.RoleEnum;
import fr.fruityhedgeh0g.keycloak.KeycloakDirectory;
import fr.fruityhedgeh0g.keycloak.KeycloakDirectory.KeycloakPerson;
import fr.fruityhedgeh0g.keycloak.KeycloakRoleMirror;
import fr.fruityhedgeh0g.repositories.UserRepository;
import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

/**
 * Makes the person named by {@code lyfia.super-admin} (a Keycloak username) the Super admin at every
 * startup, creating their row if Keycloak never announced them (ADR 0006). Nobody else can appoint the
 * Super admin, so a fresh deployment needs this to be administered at all.
 * Never stops the application: if Keycloak cannot answer, it logs and retries at the next startup.
 */
@ApplicationScoped
public class SuperAdminBootstrap {

    @Inject
    KeycloakDirectory keycloakDirectory;

    @Inject
    KeycloakRoleMirror keycloakRoleMirror;

    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "lyfia.super-admin")
    Optional<String> superAdmin;

    void onStart(@Observes StartupEvent event) {
        superAdmin.filter(username -> !username.isBlank()).ifPresent(this::appoint);
    }

    /** Makes the person with this Keycloak username the Super admin; false if they could not be found. */
    public boolean appoint(String username) {
        Optional<KeycloakPerson> found;
        try {
            found = keycloakDirectory.findByUsername(username);
        } catch (RuntimeException e) {
            Log.errorf(e, "Could not look up the Super admin %s in Keycloak; retried at the next startup", username);
            return false;
        }
        if (found.isEmpty()) {
            Log.errorf("No Keycloak user named %s: nobody was made Super admin", username);
            return false;
        }

        KeycloakPerson person = found.get();
        QuarkusTransaction.requiringNew().run(() -> {
            UserEntity user = userRepository.findByIdOptional(person.id()).orElseGet(() -> {
                UserEntity created = UserEntity.builder()
                        .userId(person.id())
                        .firstName(orElse(person.firstName(), username))
                        .lastName(orElse(person.lastName(), ""))
                        .build();
                userRepository.persist(created);
                return created;
            });
            // Above every Secteur (ADR 0004)
            user.changeRole(RoleEnum.SUPER_ADMIN);
            user.setSector(null);
        });

        try {
            keycloakRoleMirror.setRoleGroup(person.id(), RoleEnum.SUPER_ADMIN);
        } catch (RuntimeException e) {
            // ADR 0002: the database Role still applies
            Log.warnf(e, "Could not mirror the Super admin Role of %s to Keycloak", username);
        }
        Log.infof("%s is the Super admin", username);
        return true;
    }

    private static String orElse(String name, String fallback) {
        return name == null || name.isBlank() ? fallback : name;
    }
}
