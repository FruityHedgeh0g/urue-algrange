package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.model.entities.SerieEntity;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {

    @JsonView(System.class)
    private UUID eventId;

    @JsonView(System.class)
    private LocalDateTime createdAt;

    @JsonView(System.class)
    private LocalDateTime updatedAt;

    @JsonView(System.class)
    private UUID updatedBy;

    @JsonView(Basic.class)
    private String status;

    @JsonView(Basic.class)
    private String name;

    @JsonView(Basic.class)
    private String description;

    @JsonView(Basic.class)
    private LocalDateTime startDateTime;

    @JsonView(Basic.class)
    private LocalDateTime endDateTime;

    @JsonView(Full.class)
    private String latitude;

    @JsonView(Full.class)
    private String longitude;

    @JsonView(Full.class)
    private String address;

    @JsonView(Full.class)
    private String city;

    @JsonView(Full.class)
    private String country;

    @JsonView(Full.class)
    private String postalCode;

    @JsonView(Full.class)
    private String addressComplement;

    @JsonView(Basic.class)
    private SerieEntity serie;

    @JsonView(Full.class)
    private Set<UserEntity> participants;

    @JsonView(Full.class)
    private Set<UserEntity> organizers;

    @JsonView(Full.class)
    private UserEntity creator;

    public interface System{}
    public interface Basic extends System {}
    public interface Full extends Basic {}


}


