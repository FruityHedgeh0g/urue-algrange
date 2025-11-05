package fr.fruityhedgeh0g.model.entities.configurations;

import fr.fruityhedgeh0g.model.entities.AuditTemplate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "features")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeatureEntity {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
