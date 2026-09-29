package fr.fruityhedgeh0g.keycloak;

import fr.fruityhedgeh0g.enums.RoleEnum;

import java.util.UUID;

/**
 * One-way copy of a person's Role into Keycloak (ADR 0002): after the change,
 * the person belongs to the group named after their Role and to no other Role group.
 * Keycloak never drives the Role; a failure here leaves the database Role in place.
 */
public interface KeycloakRoleMirror {
    void setRoleGroup(UUID personId, RoleEnum role);
}
