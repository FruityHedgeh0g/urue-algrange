package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.entities.medias.MediaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class PostDto {

    @JsonView({Views.CreationResponse.class,Views.Basic.class})
    UUID postId;

    @JsonView({Views.Creation.class,Views.Basic.class})
    String  title;

    @JsonView(Views.Creation.class)
    String content;

    @JsonView(Views.Creation.class)
    MediaEntity banner;

    @JsonView(Views.Creation.class)
    List<MediaEntity> attachments;
}
