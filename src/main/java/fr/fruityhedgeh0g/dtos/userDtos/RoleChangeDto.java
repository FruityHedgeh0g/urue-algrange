package fr.fruityhedgeh0g.dtos.userDtos;

import fr.fruityhedgeh0g.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** {@code sectorId}: the Secteur the Super admin names when appointing an Admin or giving someone a first Secteur. */
public record RoleChangeDto(@NotNull RoleEnum role, UUID sectorId) {
}
