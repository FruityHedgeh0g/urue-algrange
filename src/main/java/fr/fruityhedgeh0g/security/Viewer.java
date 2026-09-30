package fr.fruityhedgeh0g.security;

import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.enums.RoleEnum;
import fr.fruityhedgeh0g.repositories.UserRepository;
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

    @Inject
    UserRepository userRepository;

    /** Only the Super admin sees a Secteur fermé, its Groupes and its Events (ADR 0003); nobody outside a request. */
    public boolean seesClosedSecteurs() {
        return Arc.container().requestContext().isActive() && identity.get().hasRole(RoleEnum.SUPER_ADMIN.id());
    }

    /**
     * The Secteurs this person manages (ADR 0004). Outside a request (internal calls, service tests)
     * nothing is scoped: the checks belong to what a person asks for.
     */
    public SecteurScope scope() {
        if (!Arc.container().requestContext().isActive()) return SecteurScope.EVERY_SECTEUR;
        SecurityIdentity current = identity.get();
        if (current.hasRole(RoleEnum.SUPER_ADMIN.id())) return SecteurScope.EVERY_SECTEUR;
        return DatabaseRoleAugmentor.subjectOf(current)
                .flatMap(userRepository::findByIdOptional)
                .map(UserEntity::getSector)
                .map(SecteurScope::of)
                .orElse(SecteurScope.of(null));
    }
}
