package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import fr.fruityhedgeh0g.enums.RoleTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class RoleServiceTest {

    @BeforeEach
    public void setUp() {

    }

    /** @see RoleServiceImpl#getAllRoles() **/

    @Test
    public void GetAllRoles_Success(){

    }

    @Test
    public void GetAllRoles_Failure_NotManagedException(){

    }


    /** @see RoleServiceImpl#getAllRolesFilteredByRoleType(RoleTypeEnum[]) **/

    @Test
    public void GetAllRolesFilteredByRoleType_Success(){

    }

    @Test
    public void GetAllRolesFilteredByRoleType_Failure_NotManagedException(){

    }

    @Test
    public void GetAllRolesFilteredByRoleType_Failure_ConstraintViolation(){

    }


    /** @see RoleServiceImpl#getRoleById(UUID)  **/

    @Test
    public void GetRoleById_Success(){

    }

    @Test
    public void GetRoleById_Failure_NotManagedException(){

    }

    @Test
    public void GetRoleById_Failure_UnknownResource(){

    }

    @Test
    public void GetRoleById_Failure_ConstraintViolation(){

    }


    /** @see RoleServiceImpl#internalGetRoleById(UUID) **/

    @Test
    public void InternalGetRoleById_Success(){

    }

    @Test
    public void InternalGetRoleById_Failure_NotManagedException(){

    }

    @Test
    public void InternalGetRoleById_Failure_UnknownResource(){

    }

    @Test
    public void InternalGetRoleById_Failure_ConstraintViolation(){

    }



    /** @see RoleServiceImpl#createRole(RoleDto)  **/

    @Test
    public void CreateRole_Success(){

    }

    @Test
    public void CreateRole_Failure_ConstraintViolation(){

    }

    @Test
    public void CreateRole_Failure_DuplicateResource(){

    }

    @Test
    public void CreateRole_Failure_NotManagedException(){

    }

    /** @see RoleServiceImpl#updateRole(RoleDto) **/

    @Test
    public void UpdateRole_Success(){

    }

    @Test
    public void UpdateRole_Failure_ConstraintViolation(){

    }

    @Test
    public void UpdateRole_Failure_UnknownResource(){

    }

    @Test
    public void UpdateRole_Failure_NotManagedException(){

    }

    @Test
    public void UpdateRole_Failure_DuplicateResource(){

    }

    /** @see RoleServiceImpl#deleteRole(UUID) **/

    @Test
    public void DeleteRole_Success(){

    }

    @Test
    public void DeleteRole_Failure_ConstraintViolation(){

    }

    @Test
    public void DeleteRole_Failure_UnknownResource(){

    }

    @Test
    public void DeleteRole_Failure_NotManagedException(){

    }

    @Test
    public void DeleteRole_Failure_DuplicateResource(){

    }


}
