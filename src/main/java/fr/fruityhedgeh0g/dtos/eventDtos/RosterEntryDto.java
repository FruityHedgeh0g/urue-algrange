package fr.fruityhedgeh0g.dtos.eventDtos;

import fr.fruityhedgeh0g.enums.RideModeEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/** One person on an Event's roster, as the Bureau sees it. */
public record RosterEntryDto(UUID personId, String firstName, String lastName, String phone, RideModeEnum mode, LocalDateTime signedUpAt) {
}
