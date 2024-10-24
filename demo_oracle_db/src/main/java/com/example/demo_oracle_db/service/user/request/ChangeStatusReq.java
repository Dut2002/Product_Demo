package com.example.demo_oracle_db.service.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeStatusReq {
    @NotNull
    Long id;
    @NotBlank
    String status;
}
