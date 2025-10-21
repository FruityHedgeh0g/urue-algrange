package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventEntity extends AuditTemplate {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    @Column(name = "status")
    private String status;

    @Column(name = "name")
    private String name;

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
    private String postalCode;

    @Column(name = "address_complement")
    private String addressComplement;

    @ManyToOne
    @JoinColumn(name = "serie_id")
    private SerieEntity serie;

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
