package fr.fruityhedgeh0g.model.entities.roles;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "LEGAL")
@Getter
@Setter
public class LegalRoleEntity extends RoleEntity {

}
