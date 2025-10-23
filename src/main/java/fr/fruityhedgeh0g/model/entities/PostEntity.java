package fr.fruityhedgeh0g.model.entities;

import fr.fruityhedgeh0g.model.entities.medias.MediaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostEntity extends AuditTemplate {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID postId;

    @Column(name = "title")
    private String  title;

    @Column(name = "content")
    private String content;

    @OneToOne
    @JoinColumn(name = "media_id")
    private MediaEntity banner;

    @ManyToMany
    @JoinTable(name = "post_attachments", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "media_id"))
    private List<MediaEntity> attachments;
}
