package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    @JsonView(Identifier.class)
    private UUID postId;

    @JsonView(System.class)
    private LocalDateTime createdAt;

    @JsonView(System.class)
    private LocalDateTime updatedAt;

    @JsonView(System.class)
    private UUID updatedBy;

    public interface Identifier{}
    public interface System extends Identifier {}
    public interface Basic extends System {}
    public interface Full extends Basic {}
}
