package fr.fruityhedgeh0g.entities;

import fr.fruityhedgeh0g.entities.medias.MediaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID postId;

    @Column(name = "title", nullable = false)
    @NotNull
    private String  title;

    @Column(name = "content", nullable = false)
    @NotNull
    private String content;

    @OneToOne
    @JoinColumn(name = "media_id")
    private MediaEntity banner;

    @ManyToMany
    @JoinTable(name = "post_attachments", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "media_id"))
    private List<MediaEntity> attachments;
}
