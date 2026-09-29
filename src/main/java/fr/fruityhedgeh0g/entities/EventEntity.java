package fr.fruityhedgeh0g.entities;

import fr.fruityhedgeh0g.enums.EventStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventEntity extends AuditTemplate {

    @Id
    @Column(name = "event_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    /** Manual status (Planification, Ouvert, Complet, Annulé); see {@link #currentStatus()}. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EventStatusEnum status = EventStatusEnum.PLANIFICATION;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id")
    private SectorEntity sector;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "startDateTime", nullable = false)
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

    @Column(name = "image_url")
    private String imageUrl;

    /** Optional overall maximum of Participants; beyond it, sign-ups go onto the Liste d'attente. */
    @Column(name = "max_participants")
    private Integer maxParticipants;

    /** Dates are entered and read in the association's local time, whatever the server's zone. */
    public static final ZoneId ZONE = ZoneId.of("Europe/Paris");

    /** Status as of now: an Ouvert or Complet Event reads En cours, then Archivé, from its dates. */
    public EventStatusEnum currentStatus() {
        return status.at(startDateTime, endDateTime, LocalDateTime.now(ZONE));
    }
}
