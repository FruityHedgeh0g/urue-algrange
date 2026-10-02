package fr.fruityhedgeh0g.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Event status (CONTEXT.md): Planification → Ouvert ⇄ Complet by hand, then
 * En cours and Archivé from the Event's dates; Annulé by hand before Archivé.
 * Only the manual statuses are stored; {@link #at} derives the current one.
 */
public enum EventStatusEnum {
    PLANIFICATION,
    OUVERT,
    COMPLET,
    EN_COURS,
    ARCHIVE,
    ANNULE;

    /** Statuses that follow the Event's dates once it starts. */
    private static final Set<EventStatusEnum> DATE_DRIVEN = Set.of(OUVERT, COMPLET);

    /** Name used in the API, e.g. {@code en_cours}. */
    @JsonValue
    public String id() {
        return name().toLowerCase();
    }

    /**
     * Current status of an Event stored with this status. Only an Ouvert or
     * Complet Event follows its dates; Planification and Annulé stay put.
     */
    public EventStatusEnum at(LocalDateTime start, LocalDateTime end, LocalDateTime now) {
        if (!DATE_DRIVEN.contains(this)) return this;
        if (now.isAfter(end)) return ARCHIVE;
        if (!now.isBefore(start)) return EN_COURS;
        return this;
    }

    /** Sign-ups are taken while Ouvert, and onto the Liste d'attente while Complet. */
    public boolean acceptsSignUps() {
        return this == OUVERT || this == COMPLET;
    }

    /** Archivé or Annulé: the Event is over and is no longer edited. */
    public boolean isFinal() {
        return this == ARCHIVE || this == ANNULE;
    }

    /** Manual transitions the Bureau may make from this (current) status. */
    public boolean canMoveTo(EventStatusEnum target) {
        return switch (target) {
            case OUVERT -> this == PLANIFICATION || this == COMPLET;
            case COMPLET -> this == OUVERT;
            case ANNULE -> this != ARCHIVE && this != ANNULE;
            case PLANIFICATION, EN_COURS, ARCHIVE -> false;
        };
    }
}
