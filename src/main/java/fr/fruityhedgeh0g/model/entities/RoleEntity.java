package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public abstract class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;
}
