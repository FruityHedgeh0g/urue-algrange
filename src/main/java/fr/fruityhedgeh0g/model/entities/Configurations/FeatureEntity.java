package fr.fruityhedgeh0g.model.entities.Configurations;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FeatureEntity {

    @Id
    private String name;

    private String description;

    private boolean isActive;
}
