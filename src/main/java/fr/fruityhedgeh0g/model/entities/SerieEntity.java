package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "series")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SerieEntity extends AuditTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID serieId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "serie")
    private Set<EventEntity> events;

}
