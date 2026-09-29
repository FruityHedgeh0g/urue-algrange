package fr.fruityhedgeh0g.entities;

import fr.fruityhedgeh0g.enums.RegistrationStatusEnum;
import fr.fruityhedgeh0g.enums.RideModeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A person's sign-up for an Event: a confirmed place (Participant) or a place
 * on the Event's Liste d'attente, ordered by sign-up time.
 */
@Entity
@Table(name = "event_registrations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "person_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventRegistrationEntity extends AuditTemplate {

    @Id
    @Column(name = "registration_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID registrationId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private UserEntity person;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false)
    private RideModeEnum mode;

    /** true while on the Liste d'attente; nobody moves up automatically. */
    @Column(name = "waiting", nullable = false)
    private boolean waiting;

    @Column(name = "signed_up_at", nullable = false)
    private LocalDateTime signedUpAt;

    /** A new pilote sign-up, dated now; {@code waiting} puts it on the Liste d'attente. */
    public static EventRegistrationEntity pilote(EventEntity event, UserEntity person, boolean waiting) {
        return EventRegistrationEntity.builder()
                .event(event)
                .person(person)
                .mode(RideModeEnum.PILOTE)
                .waiting(waiting)
                .signedUpAt(LocalDateTime.now(EventEntity.ZONE))
                .build();
    }

    public RegistrationStatusEnum status() {
        return waiting ? RegistrationStatusEnum.EN_ATTENTE : RegistrationStatusEnum.PARTICIPANT;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(registrationId, ((EventRegistrationEntity) o).registrationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(registrationId);
    }
}
