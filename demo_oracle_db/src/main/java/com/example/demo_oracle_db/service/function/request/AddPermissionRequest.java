package com.example.demo_oracle_db.service.function.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPermissionRequest {
    @NotNull(message = "Function is required")
    Long functionId;
    @NotBlank(message = "Permission Name is required")
    String name;
    @NotBlank(message = "End Point Back-end is required")
    String beEndPoint;
    @NotNull
    Boolean defaultPermission;
}
