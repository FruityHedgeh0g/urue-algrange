package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;


@Value
public class MediaDto {
    @JsonView(Views.IdentityOnly.class)
    UUID mediaId;

    @JsonView({Views.System.class,Views.Minimal.class})
    LocalDateTime createdAt;

    @JsonView({Views.System.class,Views.Minimal.class})
    LocalDateTime updatedAt;

    @JsonView({Views.System.class,Views.Minimal.class})
    UUID updatedBy;

    @JsonView({Views.Minimal.class,Views.Creation.class})
    String mediaType;

    @JsonView({Views.Minimal.class,Views.Creation.class})
    String fileKey;

    @JsonView({Views.Minimal.class,Views.Creation.class})
    String originalFilename;

    @JsonView({Views.Minimal.class,Views.Creation.class})
    String contentType;

    @JsonView({Views.Basic.class,Views.Creation.class})
    long fileSize;


}
