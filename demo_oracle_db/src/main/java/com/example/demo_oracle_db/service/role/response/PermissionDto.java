package com.example.demo_oracle_db.service.role.response;

import com.example.demo_oracle_db.entity.Permission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDto {
    Long id;
    String name;
    String beEndPoint;
    boolean defaultPermission;
    public PermissionDto(Permission permission) {
        id = permission.getId();
        name = permission.getName();
        beEndPoint = permission.getBeEndPoint();
        defaultPermission = permission.getDefaultPermission()==1;
    }

    public PermissionDto(Long id, String name, String beEndPoint, Integer defaultPermission) {
        this.id = id;
        this.name = name;
        this.beEndPoint = beEndPoint;
        this.defaultPermission = defaultPermission==1;
    }

}
