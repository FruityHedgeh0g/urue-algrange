package fr.fruityhedgeh0g.model.entities.roles;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "ORGANIZATIONAL")
@Getter
@Setter
public class OrganizationalRoleEntity extends RoleEntity{

}
