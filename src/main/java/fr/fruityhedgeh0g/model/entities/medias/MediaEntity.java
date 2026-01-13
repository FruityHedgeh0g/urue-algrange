package fr.fruityhedgeh0g.model.entities.medias;

import fr.fruityhedgeh0g.model.entities.AuditTemplate;
import fr.fruityhedgeh0g.model.entities.PostEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
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
    @Column(name = "media_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID mediaId;

    @Column(name = "file_key", nullable = false)
    @NotNull
    private String fileKey;

    @Column(name = "original_filename", nullable = false)
    @NotNull
    private String originalFilename;

    @Column(name = "content_type", nullable = false)
    @NotNull
    private String contentType;

    @Column(name = "file_size")
    private long fileSize;
}
