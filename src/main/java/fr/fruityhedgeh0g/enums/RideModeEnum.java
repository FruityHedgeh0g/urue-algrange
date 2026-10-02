package fr.fruityhedgeh0g.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/** How a Participant rides at an Event, chosen per Event. */
public enum RideModeEnum {
    PILOTE,
    /** Rides with a named pilote of the same Event and always follows them. */
    PASSAGER;

    @JsonValue
    public String id() {
        return name().toLowerCase();
    }
}
