package com.example.demo_oracle_db.service.function.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateFunctionRequest {
    @NotNull
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String feRoute;
}
