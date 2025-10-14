package fr.fruityhedgeh0g.model.entities.roles;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_type")
public abstract class RoleEntity {
//
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;

    @Column
    private String name;

    @Column
    private String description;
}
