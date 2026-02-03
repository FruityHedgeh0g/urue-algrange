package fr.fruityhedgeh0g.dtos.MediaDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.Views;
import lombok.Value;

import java.util.UUID;

@Value
public class MediaDto {

    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class})
    UUID mediaId;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String fileKey;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String originalFilename;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String contentType;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    long fileSize;
}
