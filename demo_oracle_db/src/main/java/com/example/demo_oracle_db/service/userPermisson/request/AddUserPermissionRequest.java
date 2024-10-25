package com.example.demo_oracle_db.service.userPermisson.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddUserPermissionRequest {
    @NotNull(message = "Function is required")
    Long roleId;
    @NotNull
    Long permissionId;
}
