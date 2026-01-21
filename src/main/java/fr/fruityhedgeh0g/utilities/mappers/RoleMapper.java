package fr.fruityhedgeh0g.utilities.mappers;

import fr.fruityhedgeh0g.dtos.RoleDto;
import fr.fruityhedgeh0g.entities.roles.LegalRoleEntity;
import fr.fruityhedgeh0g.entities.roles.OrganizationalRoleEntity;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.SubclassMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta-cdi")
public interface RoleMapper {

    @Mapping(target = "roleType", constant = "ORGANIZATIONAL")
    RoleDto toDto(OrganizationalRoleEntity entity);

    @Mapping(target = "roleType", constant = "LEGAL")
    RoleDto toDto(LegalRoleEntity entity);

    default RoleDto toDto(RoleEntity entity) {
        switch (entity) {
            case OrganizationalRoleEntity organizationalRoleEntity -> {return toDto(organizationalRoleEntity);}
            case LegalRoleEntity legalRoleEntity -> {return toDto(legalRoleEntity);}
            default -> throw new IllegalStateException("Unexpected role entity type: " + entity.getClass().getSimpleName());
        }
    }

    OrganizationalRoleEntity toOrganizationalRoleEntity(RoleDto dto);

    LegalRoleEntity toLegalRoleEntity(RoleDto dto);

    default RoleEntity toEntity(RoleDto dto) {
        switch (dto.getRoleType()){
            case "ORGANIZATIONAL" -> {return toOrganizationalRoleEntity(dto);}
            case "LEGAL" -> {return toLegalRoleEntity(dto);}
            default -> throw new IllegalStateException("Unexpected role type");
        }
    }

//    @Named("roleEntityToDto")
//    @Mapping(target = "roleType", constant = "LEGAL")
//    LegalRoleDto toDto(LegalRoleEntity entity);

//    default RoleDto toDto(RoleEntity entity) {
//        if (entity instanceof OrganizationalRoleEntity) return toDto((OrganizationalRoleEntity) entity);
//        if (entity instanceof LegalRoleEntity) return toDto((LegalRoleEntity) entity);
//        throw new IllegalStateException("Unexpected role type");
//    }
//
//
//    @Named("roleDtoToEntity")
//    LegalRoleEntity toEntity(LegalRoleDto dto);
//
//    default RoleEntity toEntity(RoleDto dto) {
//        if (dto instanceof OrganizationalRoleDto) return toEntity((OrganizationalRoleDto) dto);
//        if (dto instanceof LegalRoleDto) return toEntity((LegalRoleDto) dto);
//        throw new IllegalStateException("Unexpected role type");
//    }

//    default Set<RoleEntity> toEntitySet(Set<RoleDto> dtos){
//        return dtos.stream().map(this::toEntity).collect(Collectors.toSet());
//    }
//
//    default Set<RoleDto> toDtoSet(Set<RoleEntity> entities){
//        return entities.stream().map(this::toDto).collect(Collectors.toSet());
//    }

}
