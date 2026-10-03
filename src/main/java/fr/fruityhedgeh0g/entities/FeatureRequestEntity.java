package fr.fruityhedgeh0g.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/** A change to the site asked by the Bureau, for whoever develops it. */
@Entity
@Table(name = "feature_requests")
@NoArgsConstructor
@Getter
@Setter
public class FeatureRequestEntity extends AuditTemplate {

    @Id
    @Column(name = "request_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID requestId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requested_by", nullable = false)
    private UserEntity requestedBy;
}
