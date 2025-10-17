package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @JsonView(System.class)
    private UUID userId;

    @JsonView(System.class)
    private LocalDateTime createdAt;

    @JsonView(System.class)
    private LocalDateTime updatedAt;

    @JsonView(System.class)
    private UUID updatedBy;

    @JsonView(Basic.class)
    private GroupDto group;

    @JsonView(Full.class)
    private Set<EventDto> organizedEvents;

    @JsonView(Full.class)
    private Set<EventDto> participatedEvents;

    @JsonView(Full.class)
    private Set<EventDto> createdEvents;

    public interface System{}
    public interface Basic extends System{}
    public interface Full extends Basic{}
}
