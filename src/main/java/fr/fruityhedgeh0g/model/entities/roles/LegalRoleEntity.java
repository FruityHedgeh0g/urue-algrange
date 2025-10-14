package fr.fruityhedgeh0g.model.entities.roles;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "LEGAL")
public class LegalRoleEntity extends RoleEntity {

}
