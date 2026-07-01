package fr.fruityhedgeh0g.dtos.userDtos;

import java.util.UUID;

public record NestedUserDto(UUID userId, String firstName, String lastName) {
}
