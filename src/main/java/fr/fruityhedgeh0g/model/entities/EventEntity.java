package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

    @Column(name = "startDateTime")
    private LocalDateTime startDateTime;

    @Column(name = "endDateTime")
    private LocalDateTime endDateTime;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private int postalCode;

    @Column(name = "address_complement")
    private String addressComplement;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(name = "event_participants", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> participants;

    @ManyToMany
    @JoinTable(name = "event_organizers", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> organizers;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;
}
