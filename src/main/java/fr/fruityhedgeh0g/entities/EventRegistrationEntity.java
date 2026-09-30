package fr.fruityhedgeh0g.entities;

import fr.fruityhedgeh0g.enums.DemandeStatusEnum;
import fr.fruityhedgeh0g.enums.RegistrationStatusEnum;
import fr.fruityhedgeh0g.enums.RideModeEnum;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
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

    /** The Groupe this person rides with at this Event, once accepted or placed there. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    /** The Groupe asked for by the latest Demande de groupe, if any. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "demande_group_id")
    private GroupEntity demandeGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "demande_status")
    private DemandeStatusEnum demandeStatus;

    /** A new pending Demande de groupe; only someone outside any Groupe at this Event makes one. */
    public void requestGroup(GroupEntity requested) {
        if (group != null)
            throw new InvalidResourceException("Already riding with a Groupe at this Event.");
        demandeGroup = requested;
        demandeStatus = DemandeStatusEnum.EN_ATTENTE;
    }

    public boolean hasPendingDemande() {
        return demandeStatus == DemandeStatusEnum.EN_ATTENTE;
    }

    public void acceptDemande() {
        requirePendingDemande();
        group = demandeGroup;
        demandeStatus = DemandeStatusEnum.ACCEPTEE;
    }

    /** The person stays signed up, without a Groupe, and may make a new Demande. */
    public void refuseDemande() {
        requirePendingDemande();
        demandeStatus = DemandeStatusEnum.REFUSEE;
    }

    /** The Bureau places the person in a Groupe directly, settling any Demande. */
    public void placeIn(GroupEntity placed) {
        group = placed;
        demandeGroup = placed;
        demandeStatus = DemandeStatusEnum.ACCEPTEE;
    }

    /** Takes the person out of their Groupe; they remain signed up for the Event. */
    public void leaveGroup() {
        if (group == null)
            throw new InvalidResourceException("Not riding with a Groupe at this Event.");
        group = null;
        demandeGroup = null;
        demandeStatus = null;
    }

    private void requirePendingDemande() {
        if (!hasPendingDemande())
            throw new InvalidResourceException("No pending Demande de groupe.");
    }

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
