package fr.fruityhedgeh0g.dtos.EventDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.UserDtos.NestedUserDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.entities.UserEntity;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Value
public class EventDto {

    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class})
    UUID eventId;

    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class})
    String status;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String name;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String description;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    LocalDateTime startDateTime;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    LocalDateTime endDateTime;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    NestedUserDto creator;

    @JsonView({Views.Detailed.class})
    String latitude;

    @JsonView({Views.Detailed.class})
    String longitude;

    @JsonView({Views.Detailed.class})
    String address;

    @JsonView({Views.Detailed.class})
    String city;

    @JsonView({Views.Detailed.class})
    String country;

    @JsonView({Views.Detailed.class})
    String postalCode;

    @JsonView({Views.Detailed.class})
    String addressComplement;

    @JsonView({Views.Detailed.class})
    Set<NestedUserDto> participants;

    @JsonView({Views.Detailed.class})
    Set<NestedUserDto> organizers;

}
