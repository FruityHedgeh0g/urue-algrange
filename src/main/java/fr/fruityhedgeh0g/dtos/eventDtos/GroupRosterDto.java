package fr.fruityhedgeh0g.dtos.eventDtos;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupRefDto;

import java.util.List;

/**
 * One Groupe at an Event: its maximum (null when unlimited), who rides with it,
 * and its pending Demandes in sign-up order — the Groupe's Liste d'attente once
 * the maximum is reached.
 */
public record GroupRosterDto(GroupRefDto group, Integer maximum, List<RosterEntryDto> members, List<RosterEntryDto> demandes) {
}
