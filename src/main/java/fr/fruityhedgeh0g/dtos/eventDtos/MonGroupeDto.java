package fr.fruityhedgeh0g.dtos.eventDtos;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupRefDto;
import fr.fruityhedgeh0g.enums.EventStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** Mon groupe: the Groupe a Chef leads (null without an Affectation) and, per Event, its riders and pending Demandes. */
public record MonGroupeDto(GroupRefDto group, List<MonGroupeDto.EventRoster> events) {

    /** {@code maximum}: the Groupe's maximum at the Event, null when unlimited; {@code demandes} in sign-up order. */
    public record EventRoster(UUID eventId, String name, LocalDateTime startDateTime, EventStatusEnum status, Integer maximum,
                              List<RosterEntryDto> members, List<RosterEntryDto> demandes) {
    }
}
