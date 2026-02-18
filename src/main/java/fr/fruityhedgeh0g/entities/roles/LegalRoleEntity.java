package fr.fruityhedgeh0g.entities.roles;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "LEGAL")
@Getter
@Setter
public class LegalRoleEntity extends RoleEntity {

}
