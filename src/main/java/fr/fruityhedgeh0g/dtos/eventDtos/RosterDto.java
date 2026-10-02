package fr.fruityhedgeh0g.dtos.eventDtos;

import java.util.List;
import java.util.UUID;

/** An Event's Participants and Liste d'attente, each in sign-up order, and each Groupe of its Secteur at the Event. */
public record RosterDto(UUID eventId, Integer maxParticipants, List<RosterEntryDto> participants, List<RosterEntryDto> waiting,
                        List<GroupRosterDto> groups) {
}
