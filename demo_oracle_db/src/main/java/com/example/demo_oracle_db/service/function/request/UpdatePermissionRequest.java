package com.example.demo_oracle_db.service.function.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePermissionRequest {
    @NotNull
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String beEndPoint;
    Boolean defaultPermission;
}
