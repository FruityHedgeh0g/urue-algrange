package fr.fruityhedgeh0g.dtos.userDtos;

import fr.fruityhedgeh0g.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;

public record RoleChangeDto(@NotNull RoleEnum role) {
}
