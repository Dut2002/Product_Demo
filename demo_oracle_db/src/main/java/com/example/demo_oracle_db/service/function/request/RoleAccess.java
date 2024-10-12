package com.example.demo_oracle_db.service.function.request;

import com.example.demo_oracle_db.service.function.response.IRoleAccess;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleAccess {
    Long roleId;
    String roleName;
    boolean permission;

    public RoleAccess(IRoleAccess iRoleAccess){
        roleId = iRoleAccess.getRoleId();
        roleName = iRoleAccess.getRoleName();
        permission = iRoleAccess.getPermission().equals(1);
    }
}
