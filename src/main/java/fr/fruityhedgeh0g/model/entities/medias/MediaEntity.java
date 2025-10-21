package fr.fruityhedgeh0g.model.entities.medias;

import fr.fruityhedgeh0g.model.entities.AuditTemplate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "medias")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "media_type")
@Getter
@Setter
public abstract class MediaEntity extends AuditTemplate {

    @Id
    @Column(name = "media_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mediaId;

}
