package fr.fruityhedgeh0g.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static fr.fruityhedgeh0g.enums.EventStatusEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventStatusEnumTest {

    static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 12, 0);
    static final LocalDateTime START = NOW.minusHours(1);
    static final LocalDateTime END = NOW.plusHours(1);

    @ParameterizedTest(name = "{0} to {1}: {2}")
    @CsvSource({
            "PLANIFICATION, OUVERT, true",
            "OUVERT, COMPLET, true",
            "COMPLET, OUVERT, true",
            "PLANIFICATION, ANNULE, true",
            "OUVERT, ANNULE, true",
            "COMPLET, ANNULE, true",
            "EN_COURS, ANNULE, true",
            // Everything else is refused
            "PLANIFICATION, COMPLET, false",
            "OUVERT, PLANIFICATION, false",
            "COMPLET, PLANIFICATION, false",
            "OUVERT, EN_COURS, false",
            "OUVERT, ARCHIVE, false",
            "EN_COURS, OUVERT, false",
            "EN_COURS, COMPLET, false",
            "ARCHIVE, ANNULE, false",
            "ARCHIVE, OUVERT, false",
            "ANNULE, OUVERT, false",
            "ANNULE, ANNULE, false",
            "OUVERT, OUVERT, false",
    })
    void manualTransitions(EventStatusEnum from, EventStatusEnum to, boolean allowed) {
        assertEquals(allowed, from.canMoveTo(to));
    }

    @ParameterizedTest(name = "stored {0} before the start reads {0}")
    @CsvSource({"PLANIFICATION", "OUVERT", "COMPLET", "ANNULE"})
    void beforeTheStartTheStoredStatusApplies(EventStatusEnum stored) {
        assertEquals(stored, stored.at(NOW.plusDays(1), NOW.plusDays(2), NOW));
    }

    @ParameterizedTest(name = "stored {0} between start and end reads {1}")
    @CsvSource({"OUVERT, EN_COURS", "COMPLET, EN_COURS", "PLANIFICATION, PLANIFICATION", "ANNULE, ANNULE"})
    void betweenStartAndEnd(EventStatusEnum stored, EventStatusEnum expected) {
        assertEquals(expected, stored.at(START, END, NOW));
    }

    @ParameterizedTest(name = "stored {0} after the end reads {1}")
    @CsvSource({"OUVERT, ARCHIVE", "COMPLET, ARCHIVE", "PLANIFICATION, PLANIFICATION", "ANNULE, ANNULE"})
    void afterTheEnd(EventStatusEnum stored, EventStatusEnum expected) {
        assertEquals(expected, stored.at(NOW.minusDays(2), NOW.minusDays(1), NOW));
    }

    @Test
    void jsonIdsAreLowerCase() {
        assertEquals("en_cours", EN_COURS.id());
    }
}
