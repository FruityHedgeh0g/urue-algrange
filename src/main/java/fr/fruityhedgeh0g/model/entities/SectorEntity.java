package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sectors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectorEntity extends AuditTemplate {

    @Id
    @Column(name = "sector_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sectorId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "sector")
    private Set<GroupEntity> groups;

    public void addGroup(GroupEntity group) {
        groups.add(group);
        group.setSector(this);
    }
}
