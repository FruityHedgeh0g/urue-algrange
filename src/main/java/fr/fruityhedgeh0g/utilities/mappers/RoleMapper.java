package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.RoleDto;
import fr.fruityhedgeh0g.model.entities.roles.LegalRoleEntity;
import fr.fruityhedgeh0g.model.entities.roles.OrganizationalRoleEntity;
import fr.fruityhedgeh0g.model.entities.roles.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta-cdi")
public interface RoleMapper {

    RoleDto toDto(OrganizationalRoleEntity entity);
    RoleDto toDto(LegalRoleEntity entity);

    OrganizationalRoleEntity toOrganizationalRoleEntity(RoleDto dto);
    LegalRoleEntity toLegalRoleEntity(RoleDto dto);

    default RoleDto toDto(RoleEntity entity){
        return switch (entity.getRoleType()){
            case "ORGANIZATIONAL" -> toDto((OrganizationalRoleEntity) entity);
            case "LEGAL" -> toDto((LegalRoleEntity) entity);
            default -> throw new IllegalStateException("Unexpected value: " + entity.getRoleType());
        };
    }

    default RoleEntity toEntity(RoleDto dto){
        return switch (dto.getRoleType()){
            case "ORGANIZATIONAL" -> toOrganizationalRoleEntity(dto);
            case "LEGAL" -> toLegalRoleEntity(dto);
            default -> throw new IllegalStateException("Unexpected value: " + dto.getRoleType());
        };
    }


}
