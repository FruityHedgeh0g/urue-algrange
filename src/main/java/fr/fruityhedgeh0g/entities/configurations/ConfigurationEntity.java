package fr.fruityhedgeh0g.entities.configurations;

import fr.fruityhedgeh0g.entities.AuditTemplate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@Entity
@Table(name = "configurations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfigurationEntity extends AuditTemplate {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "config_value", nullable = false)
    private String value;
}
