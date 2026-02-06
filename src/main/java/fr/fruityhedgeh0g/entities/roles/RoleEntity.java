package fr.fruityhedgeh0g.entities.roles;

import fr.fruityhedgeh0g.entities.AuditTemplate;
import fr.fruityhedgeh0g.enums.RoleTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false, insertable = false, updatable = false)
    private RoleTypeEnum roleType;

//    @ManyToMany(mappedBy = "roles")
//    private Set<UserEntity> users;
}
