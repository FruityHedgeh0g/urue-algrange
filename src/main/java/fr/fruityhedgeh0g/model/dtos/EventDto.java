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
    @JsonView(Views.identityOnly.class)
    private UUID eventId;

    @JsonView(Views.system.class)
    private LocalDateTime createdAt;

    @JsonView(Views.system.class)
    private LocalDateTime updatedAt;

    @JsonView(Views.system.class)
    private UUID updatedBy;

    @JsonView(Views.minimal.class)
    private String status;

    @JsonView({Views.minimal.class,Views.creation.class})
    private String name;

    @JsonView({Views.basic.class,Views.creation.class})
    private String description;

    @JsonView({Views.minimal.class,Views.creation.class})
    private LocalDateTime startDateTime;

    @JsonView({Views.minimal.class,Views.creation.class})
    private LocalDateTime endDateTime;

    @JsonView({Views.basic.class,Views.creation.class})
    private String latitude;

    @JsonView({Views.basic.class,Views.creation.class})
    private String longitude;

    @JsonView({Views.basic.class,Views.creation.class})
    private String address;

    @JsonView({Views.basic.class,Views.creation.class})
    private String city;

    @JsonView({Views.basic.class,Views.creation.class})
    private String country;

    @JsonView({Views.basic.class,Views.creation.class})
    private String postalCode;

    @JsonView({Views.basic.class,Views.creation.class})
    private String addressComplement;

    @JsonView(Views.basic.class)
    @JsonSerialize(using = ViewSerializers.class)
    private SerieEntity serie;

    @JsonView(Views.full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<UserEntity> participants;

    @JsonView(Views.full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private Set<UserEntity> organizers;

    @JsonView(Views.full.class)
    @JsonSerialize(using = ViewSerializers.class)
    private UserEntity creator;



}


