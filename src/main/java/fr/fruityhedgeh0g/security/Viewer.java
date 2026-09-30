package fr.fruityhedgeh0g.security;

import fr.fruityhedgeh0g.enums.RoleEnum;
import io.quarkus.arc.Arc;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/** What the person making the current request may see, beyond what their Role opens. */
@ApplicationScoped
public class Viewer {

    @Inject
    Instance<SecurityIdentity> identity;

    /** Only the Super admin sees a Secteur fermé, its Groupes and its Events (ADR 0003); nobody outside a request. */
    public boolean seesClosedSecteurs() {
        return Arc.container().requestContext().isActive() && identity.get().hasRole(RoleEnum.SUPER_ADMIN.id());
    }
}
