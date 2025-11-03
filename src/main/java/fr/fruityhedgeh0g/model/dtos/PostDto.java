package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.entities.medias.MediaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import javax.print.attribute.standard.Media;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Value
public class PostDto {
    @JsonView(Views.IdentityOnly.class)
    UUID postId;

    @JsonView(Views.System.class)
    LocalDateTime createdAt;

    @JsonView(Views.System.class)
    LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    UUID updatedBy;

    @JsonView({Views.Minimal.class,Views.Creation.class})
    String  title;

    @JsonView({Views.Basic.class,Views.Creation.class})
    String content;

    @JsonView({Views.Full.class,Views.Creation.class})
    MediaDto banner;

    @JsonView({Views.Basic.class,Views.Creation.class})
    List<MediaDto> attachments;

}
