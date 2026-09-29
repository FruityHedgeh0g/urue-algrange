package fr.fruityhedgeh0g.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/** Where a sign-up stands: a confirmed place (Participant) or the Event's Liste d'attente. */
public enum RegistrationStatusEnum {
    PARTICIPANT,
    EN_ATTENTE;

    @JsonValue
    public String id() {
        return name().toLowerCase();
    }
}
