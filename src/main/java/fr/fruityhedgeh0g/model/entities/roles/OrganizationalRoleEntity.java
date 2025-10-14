package fr.fruityhedgeh0g.model.entities.roles;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "ORGANIZATIONAL")
public class OrganizationalRoleEntity extends RoleEntity{

}
