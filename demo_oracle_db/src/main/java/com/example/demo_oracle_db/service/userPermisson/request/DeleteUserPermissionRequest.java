package com.example.demo_oracle_db.service.userPermisson.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeleteUserPermissionRequest {
    @NotNull
    Long roleId;
    @NotNull
    Long permissionId;
}
