package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.model.entities.SeriesEntity;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Value
public class EventDto {
    @JsonView(Views.IdentityOnly.class)
    UUID eventId;

    @JsonView(Views.System.class)
    LocalDateTime createdAt;

    @JsonView(Views.System.class)
    LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    UUID updatedBy;

    @JsonView(Views.Minimal.class)
    String status;

    @JsonView({Views.Minimal.class, Views.Creation.class})
    String name;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String description;

    @JsonView({Views.Minimal.class, Views.Creation.class})
    LocalDateTime startDateTime;

    @JsonView({Views.Minimal.class, Views.Creation.class})
    LocalDateTime endDateTime;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String latitude;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String longitude;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String address;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String city;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String country;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String postalCode;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String addressComplement;

    @JsonView(Views.Basic.class)
    @JsonSerialize(using = ViewSerializers.class)
    SeriesEntity serie;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<UserEntity> participants;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<UserEntity> organizers;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    UserEntity creator;



}


