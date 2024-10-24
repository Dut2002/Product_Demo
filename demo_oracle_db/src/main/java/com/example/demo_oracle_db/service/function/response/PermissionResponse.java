package com.example.demo_oracle_db.service.function.response;

import com.example.demo_oracle_db.entity.Permission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionResponse {
    Long id;
    String name;
    String beEndPoint;
    String feEndPoint;

    public PermissionResponse(Permission permission) {
        this.id = permission.getId();
        this.name = permission.getName();
        this.beEndPoint = permission.getBeEndPoint();
    }
}
