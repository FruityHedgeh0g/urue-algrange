package fr.fruityhedgeh0g.model.entities.medias;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "medias")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "media_type")
public abstract class MediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mediaId;
}
