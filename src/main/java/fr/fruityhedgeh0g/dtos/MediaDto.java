package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class MediaDto {

    @JsonView({Views.CreationResponse.class,Views.Basic.class})
    UUID mediaId;

    @JsonView({Views.Creation.class,Views.Basic.class})
    String fileKey;

    @JsonView(Views.Creation.class)
    String originalFilename;

    @JsonView({Views.Creation.class,Views.Basic.class})
    String contentType;

    @JsonView(Views.Creation.class)
    long fileSize;
}
