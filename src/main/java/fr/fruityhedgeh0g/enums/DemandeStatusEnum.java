package fr.fruityhedgeh0g.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/** Where a Demande de groupe stands; it stays pending until the Chef de groupe or the Bureau decides. */
public enum DemandeStatusEnum {
    EN_ATTENTE,
    ACCEPTEE,
    REFUSEE;

    @JsonValue
    public String id() {
        return name().toLowerCase();
    }
}
