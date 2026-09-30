package fr.fruityhedgeh0g.dtos.eventDtos;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupRefDto;
import fr.fruityhedgeh0g.enums.RideModeEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/** One person on an Event's roster, as the Bureau or their Chef de groupe sees it. */
public record RosterEntryDto(UUID personId, String firstName, String lastName, String phone, RideModeEnum mode,
                             LocalDateTime signedUpAt, GroupRefDto group, DemandeDto demande) {
}
