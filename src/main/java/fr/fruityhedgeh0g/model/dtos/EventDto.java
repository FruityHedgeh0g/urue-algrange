package fr.fruityhedgeh0g.model.dtos;

import fr.fruityhedgeh0g.model.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class EventDto {

    private UUID eventId;

    private String status;

    private String description;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String latitude;

    private String longitude;

    private String address;

    private String city;

    private String country;

    private int postalCode;

    private String addressComplement;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<UserDto> participants;

    private Set<UserDto> organizers;

    private UserDto creator;
}
