package fr.fruityhedgeh0g.model.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.fruityhedgeh0g.model.entities.SerieEntity;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import fr.fruityhedgeh0g.utilities.serializers.ViewSerializers;
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
    @JsonView(Views.IdentityOnly.class)
    private UUID eventId;

    @JsonView(Views.System.class)
    private LocalDateTime createdAt;

    @JsonView(Views.System.class)
    private LocalDateTime updatedAt;

    @JsonView(Views.System.class)
    private UUID updatedBy;

    @JsonView(Views.Minimal.class)
    private String status;

    @JsonView({Views.Minimal.class, Views.Creation.class})
    private String name;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String description;

    @JsonView({Views.Minimal.class, Views.Creation.class})
    private LocalDateTime startDateTime;

    @JsonView({Views.Minimal.class, Views.Creation.class})
    private LocalDateTime endDateTime;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String latitude;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String longitude;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String address;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String city;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String country;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String postalCode;

    @JsonView({Views.Basic.class, Views.Creation.class})
    private String addressComplement;

    @JsonView(Views.Basic.class)
    @JsonSerialize(using = ViewSerializers.class)
    private SerieEntity serie;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<UserEntity> participants;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<UserEntity> organizers;

    @JsonView(Views.Full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private UserEntity creator;



}


