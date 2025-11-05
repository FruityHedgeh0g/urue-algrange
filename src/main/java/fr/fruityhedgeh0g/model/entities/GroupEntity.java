package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupEntity extends AuditTemplate {

    @Id
    @Column(name = "group_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID groupId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "group")
    private Set<UserEntity> members;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private SectorEntity sector;

    @PreRemove
    private void preRemove() {
        members.forEach(member -> member.setGroup(null));
    }

}
