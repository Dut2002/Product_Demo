package com.example.demo_oracle_db.service.role.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleReq {
    Long id;
    @Size(max = 255)
    @NotBlank
    String name;
}
