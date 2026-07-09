package fr.fruityhedgeh0g.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    private Set<UserEntity> members;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id")
    private SectorEntity sector;

    public void addMember(UserEntity member) {
        if (members == null) members = new HashSet<>();
        members.add(member);
        member.setGroup(this);
    }

    public void removeMember(UserEntity member) {
        members.remove(member);
        member.setGroup(null);
    }

    public Set<UserEntity> getMembers() {
        if (members == null) members = new HashSet<>();
        return members;
    }
//    @PreRemove
//    private void preRemove() {
//        members.forEach(member -> member.setGroup(null));
//    }

}
