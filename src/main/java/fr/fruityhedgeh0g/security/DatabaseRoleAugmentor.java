package fr.fruityhedgeh0g.security;

import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.enums.RoleEnum;
import fr.fruityhedgeh0g.repositories.UserRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Replaces the roles carried by the token with the person's Role read from the
 * database (ADR 0002), expanded to every Role below it so that
 * {@code @RolesAllowed("membre")} means "membre and above".
 * A person unknown to the database holds no Role.
 */
@ApplicationScoped
public class DatabaseRoleAugmentor implements SecurityIdentityAugmentor {

    @Inject
    UserRepository userRepository;

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        if (identity.isAnonymous()) {
            return Uni.createFrom().item(identity);
        }
        return context.runBlocking(() -> withDatabaseRole(identity));
    }

    private SecurityIdentity withDatabaseRole(SecurityIdentity identity) {
        Optional<RoleEnum> role = subjectOf(identity).flatMap(this::roleOf);
        return new QuarkusSecurityIdentity.Builder()
                .setPrincipal(identity.getPrincipal())
                .addCredentials(identity.getCredentials())
                .addAttributes(identity.getAttributes())
                .addPermissionChecker(identity::checkPermission)
                .addRoles(role.map(RoleEnum::grantedRoleIds).orElseGet(Set::of))
                .build();
    }

    private Optional<RoleEnum> roleOf(UUID subject) {
        return QuarkusTransaction.joiningExisting().call(
                () -> userRepository.findByIdOptional(subject).map(UserEntity::getRole)
        );
    }

    /** The person's id, from the token's subject. */
    static Optional<UUID> subjectOf(SecurityIdentity identity) {
        if (!(identity.getPrincipal() instanceof JsonWebToken token) || token.getSubject() == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(token.getSubject()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
