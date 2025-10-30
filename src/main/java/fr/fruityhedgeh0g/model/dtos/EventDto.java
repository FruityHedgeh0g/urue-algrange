package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    @JsonView(Views.System.class)
    UUID eventId;

    @JsonView(Views.System.class)
    LocalDateTime createdAt;

    @JsonView(Views.System.class)
    LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    UUID updatedBy;

    @JsonView(Views.Basic.class)
    String status;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String name;

    @JsonView({Views.Basic.class, Views.Creation.class})
    String description;

    @JsonView({Views.Basic.class, Views.Creation.class})
    LocalDateTime startDateTime;

    @JsonView({Views.Basic.class, Views.Creation.class})
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

    @JsonView(Views.Extended.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<UserEntity> participants;

    @JsonView(Views.Extended.class)
    Set<UserEntity> organizers;

    @JsonView(Views.Extended.class)
    UserEntity creator;


}


