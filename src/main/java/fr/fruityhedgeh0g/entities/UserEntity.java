package fr.fruityhedgeh0g.entities;

import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity extends AuditTemplate{


    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    //TODO: faire une estimation de l'utilité de garder les rôles en EAGER
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;

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

    public void addRole(RoleEntity role){
        this.roles.add(role);
        role.addUser(this);
    }

    public void removeRole(RoleEntity role){
        this.roles.remove(role);
        role.removeUser(this);
    }

}
