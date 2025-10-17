package fr.fruityhedgeh0g.model.entities;

import fr.fruityhedgeh0g.model.entities.roles.RoleEntity;
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

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> accreditations;

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
