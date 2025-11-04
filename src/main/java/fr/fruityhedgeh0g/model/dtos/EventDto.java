package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Value
public class EventDto {
    @JsonView(GlobalViews.IdentifierOnly.class)
    UUID eventId;

//    @JsonView(Views.System.class)
//    LocalDateTime createdAt;
//
//    @JsonView(Views.System.class)
//    LocalDateTime updatedAt;
//
//    @JsonView(Views.System.class)
//    UUID updatedBy;

    @JsonView(Basic.class)
    String status;

    @JsonView({Basic.class, Creation.class})
    String name;

    @JsonView({Basic.class, Creation.class})
    String description;

    @JsonView({Basic.class, Creation.class})
    LocalDateTime startDateTime;

    @JsonView({Basic.class, Creation.class})
    LocalDateTime endDateTime;

    @JsonView({Basic.class, Creation.class})
    String latitude;

    @JsonView({Basic.class, Creation.class})
    String longitude;

    @JsonView({Basic.class, Creation.class})
    String address;

    @JsonView({Basic.class, Creation.class})
    String city;

    @JsonView({Basic.class, Creation.class})
    String country;

    @JsonView({Basic.class, Creation.class})
    String postalCode;

    @JsonView({Basic.class, Creation.class})
    String addressComplement;

    @JsonView(Extended.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<UserEntity> participants;

    @JsonView(Extended.class)
    @JsonSerialize(using = ViewSerializers.class)
    Set<UserEntity> organizers;

    @JsonView(Extended.class)
    @JsonSerialize(using = ViewSerializers.class)
    UserDto creator;

    public interface Creation {}
    public interface Basic extends GlobalViews.IdentifierOnly {}
    public interface Extended extends Basic {}

}


