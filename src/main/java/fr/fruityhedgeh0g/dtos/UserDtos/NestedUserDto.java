package fr.fruityhedgeh0g.dtos.UserDtos;

import java.util.UUID;

public record NestedUserDto(UUID userId, String firstName, String lastName) {
}
