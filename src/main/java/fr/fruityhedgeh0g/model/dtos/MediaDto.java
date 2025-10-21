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
public class MediaDto {
    @JsonView(Views.identityOnly.class)
    private UUID mediaId;

    @JsonView(Views.system.class)
    private LocalDateTime createdAt;

    @JsonView(Views.system.class)
    private LocalDateTime updatedAt;

    @JsonView(Views.system.class)
    private UUID updatedBy;

    @JsonView(Views.system.class)
    private String mediaType;


}
