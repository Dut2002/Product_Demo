package com.example.demo_oracle_db.service.userPermisson.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddNewUserPermissionRequest {
    Long roleId;
    Long functionId;
    String name;
    String beEndPoint;
    Boolean defaultPermission;
}
