package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
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

}
