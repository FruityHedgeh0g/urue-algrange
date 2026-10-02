package fr.fruityhedgeh0g.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/** A Post is written as Brouillon, seen only by the Bureau, then Publié for everyone (CONTEXT.md). */
public enum PostStatusEnum {
    BROUILLON,
    PUBLIE;

    /** Name used in the API, e.g. {@code publie}. */
    @JsonValue
    public String id() {
        return name().toLowerCase();
    }
}
