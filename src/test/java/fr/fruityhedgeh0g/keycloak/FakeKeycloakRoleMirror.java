package fr.fruityhedgeh0g.keycloak;

import fr.fruityhedgeh0g.enums.RoleEnum;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/** Records every mirrored Role instead of calling Keycloak; can be told to fail. */
@Mock
@ApplicationScoped
public class FakeKeycloakRoleMirror implements KeycloakRoleMirror {

    public record Call(UUID personId, RoleEnum role) {}

    private final List<Call> calls = new CopyOnWriteArrayList<>();
    private volatile boolean failing;

    @Override
    public void setRoleGroup(UUID personId, RoleEnum role) {
        calls.add(new Call(personId, role));
        if (failing) {
            throw new IllegalStateException("Keycloak unreachable");
        }
    }

    public List<Call> calls() {
        return List.copyOf(calls);
    }

    public void failing(boolean failing) {
        this.failing = failing;
    }

    public void reset() {
        calls.clear();
        failing = false;
    }
}
