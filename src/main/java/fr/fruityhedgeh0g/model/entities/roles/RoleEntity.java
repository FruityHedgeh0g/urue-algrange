package fr.fruityhedgeh0g.model.entities.roles;

import fr.fruityhedgeh0g.model.entities.AuditTemplate;
import fr.fruityhedgeh0g.model.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role_type")
@Getter
@Setter
public abstract class RoleEntity extends AuditTemplate {
//
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;
}
