package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.model.dtos.RoleDto;
import fr.fruityhedgeh0g.model.entities.roles.LegalRoleEntity;
import fr.fruityhedgeh0g.model.entities.roles.OrganizationalRoleEntity;
import fr.fruityhedgeh0g.model.entities.roles.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "jakarta-cdi")
public interface RoleMapper {
    @Named("organizationalRoleEntityToDto")
    RoleDto toDto(OrganizationalRoleEntity entity);

    @Named("legalRoleEntityToDto")
    RoleDto toDto(LegalRoleEntity entity);

    @Named("roleDtoToLegalRoleEntity")
    OrganizationalRoleEntity toOrganizationalRoleEntity(RoleDto dto);

    @Named("roleDtoToOrganizationalRoleEntity")
    LegalRoleEntity toLegalRoleEntity(RoleDto dto);

    default RoleDto toDto(RoleEntity entity){
        RoleDto dto;
        if (entity instanceof OrganizationalRoleEntity){
            dto = toDto((OrganizationalRoleEntity) entity);
            dto.setRoleType("ORGANIZATIONAL");
            return dto;
        }else if (entity instanceof LegalRoleEntity){
            dto = toDto((LegalRoleEntity) entity);
            dto.setRoleType("LEGAL");
            return dto;
        }else throw new IllegalStateException("Unexpected value: " + entity.getClass());
    }

    default RoleEntity toEntity(RoleDto dto){
        return switch (dto.getRoleType()){
            case "ORGANIZATIONAL" -> toOrganizationalRoleEntity(dto);
            case "LEGAL" -> toLegalRoleEntity(dto);
            default -> throw new IllegalStateException("Unexpected value: " + dto.getRoleType());
        };
    }


}
