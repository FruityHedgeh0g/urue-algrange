package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Value
public class EventDto {


    @JsonView(Views.CreationResponse.class)
    UUID eventId;

    @JsonView(Views.Creation.class)
    String status;

    @JsonView(Views.Creation.class)
    String name;

    @JsonView(Views.Creation.class)
    String description;

    @JsonView(Views.Creation.class)
    LocalDateTime startDateTime;

    @JsonView(Views.Creation.class)
    LocalDateTime endDateTime;

    String latitude;

    String longitude;

    String address;

    String city;

    String country;

    String postalCode;

    String addressComplement;

    Set<UserEntity> participants;

    Set<UserEntity> organizers;

    @JsonView(Views.Creation.class)
    UserEntity creator;
}
