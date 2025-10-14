package fr.fruityhedgeh0g.model.entities;

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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    @Setter
    private UUID userId;

    //private Set<RoleEntity> accreditations;

    @ManyToMany(mappedBy = "organizers")
    private Set<EventEntity> organizedEvents;

    @ManyToMany(mappedBy = "participants")
    private Set<EventEntity> participatedEvents;

    @OneToMany(mappedBy = "creator")
    private Set<EventEntity> createdEvents;
}
