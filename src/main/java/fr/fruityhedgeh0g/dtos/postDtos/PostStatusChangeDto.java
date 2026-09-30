package fr.fruityhedgeh0g.dtos.postDtos;

import fr.fruityhedgeh0g.enums.PostStatusEnum;
import jakarta.validation.constraints.NotNull;

/** Publishes ({@code publie}) or unpublishes ({@code brouillon}) a Post. */
public record PostStatusChangeDto(@NotNull PostStatusEnum status) {
}
