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
    @JsonView(GlobalViews.IdentifierOnly.class)
    UUID mediaId;

//    @JsonView({Views.System.class,Views.Minimal.class})
//    LocalDateTime createdAt;
//
//    @JsonView({Views.System.class,Views.Minimal.class})
//    LocalDateTime updatedAt;
//
//    @JsonView({Views.System.class,Views.Minimal.class})
//    UUID updatedBy;

    @JsonView({Basic.class,Creation.class})
    String mediaType;

    @JsonView({Basic.class,Creation.class})
    String fileKey;

    @JsonView({Basic.class,Creation.class})
    String originalFilename;

    @JsonView({Basic.class,Creation.class})
    String contentType;

    @JsonView({Basic.class,Creation.class})
    long fileSize;

    public interface Creation {}
    public interface Basic extends GlobalViews.IdentifierOnly {}
}
