package fr.fruityhedgeh0g.keycloak;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** A realm held in memory instead of Keycloak; can be told to fail. */
@Mock
@ApplicationScoped
public class FakeKeycloakDirectory implements KeycloakDirectory {

    private final Map<String, KeycloakPerson> people = new ConcurrentHashMap<>();
    private volatile boolean failing;

    @Override
    public Optional<KeycloakPerson> findByUsername(String username) {
        if (failing) {
            throw new IllegalStateException("Keycloak unreachable");
        }
        return Optional.ofNullable(people.get(username));
    }

    public void register(String username, KeycloakPerson person) {
        people.put(username, person);
    }

    public void failing(boolean failing) {
        this.failing = failing;
    }

    public void reset() {
        people.clear();
        failing = false;
    }
}
