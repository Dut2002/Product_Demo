package com.example.demo_oracle_db.service.function.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddFunctionRequest {
    @NotBlank
    String name;
    @NotBlank
    String feRoute;

}
