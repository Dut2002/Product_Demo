package com.example.demo_oracle_db.service.userPermisson.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddUserNewFunction {

    @NotNull
    Long roleId;
    @NotBlank
    String functionName;
    @NotBlank
    String feRoute;
}
