package fr.fruityhedgeh0g.dtos.eventDtos;

import java.util.List;
import java.util.UUID;

/** An Event's Participants and Liste d'attente, each in sign-up order. */
public record RosterDto(UUID eventId, Integer maxParticipants, List<RosterEntryDto> participants, List<RosterEntryDto> waiting) {
}
