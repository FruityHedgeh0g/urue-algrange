package fr.fruityhedgeh0g.model.entities.configurations;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configurations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfigurationEntity {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;
}
