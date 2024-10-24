package com.example.demo_oracle_db.service.role.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDto {
    Long permissionId;
    String name;
    Long functionId;

    public PermissionDto(IPermissionDto permissionDto) {
        permissionId = permissionDto.getId();
        name = permissionDto.getName();
        functionId = permissionDto.getFunctionId();
    }
}
