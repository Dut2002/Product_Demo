package com.example.demo_oracle_db.service.function.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeletePermissionRequest {
    @NotNull
    Long id;
    Boolean confirmRequired;
}
