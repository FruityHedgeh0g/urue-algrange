package fr.fruityhedgeh0g.dtos.eventDtos;

import fr.fruityhedgeh0g.enums.EventStatusEnum;
import jakarta.validation.constraints.NotNull;

public record EventStatusChangeDto(@NotNull EventStatusEnum status) {
}
