package fr.fruityhedgeh0g.entities;

import fr.fruityhedgeh0g.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserEntity extends AuditTemplate{


    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'BENEVOLE'")
    @Column(name = "role", nullable = false)
    private RoleEnum role = RoleEnum.BENEVOLE;

    /** The one Bureau member presiding over the association; grants no access. */
    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "president", nullable = false)
    private boolean president = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "group_id")
    private GroupEntity group;

    //TODO : Gérer le N+1
    @ManyToMany(mappedBy = "organizers", fetch = FetchType.LAZY)
    private Set<EventEntity> organizedEvents;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private Set<EventEntity> participatedEvents;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private Set<EventEntity> createdEvents;

    /** Changes the Role; only a Bureau member can stay Président. */
    public void changeRole(RoleEnum role) {
        this.role = role;
        if (role != RoleEnum.BUREAU)
            this.president = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(group, that.group) && Objects.equals(organizedEvents, that.organizedEvents) && Objects.equals(participatedEvents, that.participatedEvents) && Objects.equals(createdEvents, that.createdEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
