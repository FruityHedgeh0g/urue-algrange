package fr.fruityhedgeh0g.dtos.featureDtos;

import fr.fruityhedgeh0g.entities.FeatureRequestEntity;

import java.time.LocalDateTime;
import java.util.UUID;

/** A feature request; {@code requestedBy} is the asker's name. */
public record FeatureRequestDto(UUID id, String title, String description, LocalDateTime createdAt, String requestedBy) {

    public static FeatureRequestDto of(FeatureRequestEntity request) {
        return new FeatureRequestDto(request.getRequestId(), request.getTitle(), request.getDescription(), request.getCreatedAt(),
                request.getRequestedBy().getFirstName() + " " + request.getRequestedBy().getLastName());
    }

    /** What the Bureau writes. */
    public record Input(String title, String description) {}
}
