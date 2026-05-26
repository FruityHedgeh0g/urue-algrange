package fr.fruityhedgeh0g.dtos.mediaDtos;

import java.util.UUID;

public record NestedMediaDto(UUID mediaId, String fileKey, String originalFilename, String contentType, long fileSize) {
}
