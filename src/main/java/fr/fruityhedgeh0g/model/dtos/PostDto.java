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
    @JsonView(GlobalViews.IdentifierOnly.class)
    UUID postId;

    @JsonView(Basic.class)
    LocalDateTime createdAt;

    @JsonView(Basic.class)
    LocalDateTime updatedAt;

//    @JsonView(Views.System.class)
//    UUID updatedBy;

    @JsonView({Basic.class,Creation.class})
    String  title;

    @JsonView({Extended.class,Creation.class})
    String content;

    @JsonView({Extended.class,Creation.class})
    MediaDto banner;

    @JsonView({Extended.class,Creation.class})
    List<MediaDto> attachments;

    public interface Creation {}
    public interface Basic extends GlobalViews.IdentifierOnly {}
    public interface Extended extends Basic {}
}
