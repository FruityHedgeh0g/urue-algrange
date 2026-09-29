package fr.fruityhedgeh0g.dtos.eventDtos;

import fr.fruityhedgeh0g.enums.RegistrationStatusEnum;
import fr.fruityhedgeh0g.enums.RideModeEnum;

import java.time.LocalDateTime;
import java.util.UUID;

/** One's own sign-up for an Event, as shown in "Mes événements". */
public record RegistrationDto(UUID eventId, RideModeEnum mode, RegistrationStatusEnum status, LocalDateTime signedUpAt) {
}
