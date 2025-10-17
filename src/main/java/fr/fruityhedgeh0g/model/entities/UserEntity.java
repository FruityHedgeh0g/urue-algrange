package fr.fruityhedgeh0g.model.entities;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity extends AuditTemplate{

    @Id
    private UUID userId;

    //private Set<RoleEntity> accreditations;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;


    @ManyToMany(mappedBy = "organizers")
    private Set<EventEntity> organizedEvents;

    @ManyToMany(mappedBy = "participants")
    private Set<EventEntity> participatedEvents;

    @OneToMany(mappedBy = "creator")
    private Set<EventEntity> createdEvents;
}
