package fr.fruityhedgeh0g.model.entities.Configurations;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationEntity {

    @Id
    private String name;

    private String description;
}
