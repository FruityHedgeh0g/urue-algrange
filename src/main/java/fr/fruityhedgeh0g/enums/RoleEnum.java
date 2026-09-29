package fr.fruityhedgeh0g.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A person's single Role, declared from the lowest to the highest (ADR 0001).
 * A Role grants everything the lower Roles grant.
 */
public enum RoleEnum {
    VISITEUR,
    BENEVOLE,
    MEMBRE,
    CHEF_DE_GROUPE,
    BUREAU,
    ADMIN,
    SUPER_ADMIN;

    /** Name used in the API and in {@code @RolesAllowed}, e.g. {@code chef_de_groupe}. */
    @JsonValue
    public String id() {
        return name().toLowerCase();
    }

    public boolean isAtLeast(RoleEnum required) {
        return compareTo(required) >= 0;
    }

    /** This Role and every Role below it, as {@code @RolesAllowed} names. */
    public Set<String> grantedRoleIds() {
        return Arrays.stream(values())
                .filter(this::isAtLeast)
                .map(RoleEnum::id)
                .collect(Collectors.toSet());
    }
}
