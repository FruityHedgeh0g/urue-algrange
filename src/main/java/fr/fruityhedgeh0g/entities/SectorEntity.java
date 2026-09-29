package fr.fruityhedgeh0g.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "sectors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SectorEntity extends AuditTemplate {

    @Id
    @Column(name = "sector_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sectorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "sector", fetch = FetchType.LAZY)
    private Set<GroupEntity> groups;

    public void addGroup(GroupEntity group) {
        if (groups == null) groups = new HashSet<>();
        groups.add(group);
        group.setSector(this);
    }

    public void removeGroup(GroupEntity group) {
        groups.remove(group);
        group.setSector(null);
    }

    public Set<GroupEntity> getGroups() {
        if (groups == null) groups = new HashSet<>();
        return groups;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SectorEntity that = (SectorEntity) o;
        return Objects.equals(sectorId, that.sectorId) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sectorId);
    }

    //@PreRemove
    //private void preRemove() {groups.forEach(group -> group.setSector(null));}
}
