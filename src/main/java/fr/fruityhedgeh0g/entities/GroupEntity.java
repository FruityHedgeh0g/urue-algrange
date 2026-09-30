package fr.fruityhedgeh0g.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GroupEntity extends AuditTemplate {

    @Id
    @Column(name = "group_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID groupId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    /** The part of the Secteur this Groupe covers. */
    @Column(name = "area")
    private String area;

    /** Current Affectation: at most one Chef per Groupe, one Groupe per Chef. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chef_id", unique = true)
    private UserEntity chef;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id")
    private SectorEntity sector;

    /** true while the Groupe's Secteur is fermé. */
    public boolean isInClosedSector() {
        return sector != null && sector.isClosed();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GroupEntity that = (GroupEntity) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(sector, that.sector);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(groupId);
    }
}
