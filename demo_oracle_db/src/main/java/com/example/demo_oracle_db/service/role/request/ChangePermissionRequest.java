package com.example.demo_oracle_db.service.role.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChangePermissionRequest {

    @NotNull(message = "Role is required")
    Long roleId;
    @NotNull(message = "Status is required")
    List<PermissionRequest> permissions;
}
