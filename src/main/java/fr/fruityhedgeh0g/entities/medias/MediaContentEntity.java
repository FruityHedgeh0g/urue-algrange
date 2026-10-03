package fr.fruityhedgeh0g.entities.medias;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** A media's file, stored in the database apart from its description, so listing medias never loads it (ADR 0008). */
@Entity
@Table(name = "media_contents")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MediaContentEntity {

    @Id
    @Column(name = "media_id", nullable = false)
    private UUID mediaId;

    /** bytea in PostgreSQL. */
    @Column(name = "content", nullable = false, length = MediaContentEntity.MAX_SIZE)
    private byte[] content;

    public static final int MAX_SIZE = 8 * 1024 * 1024;
}
