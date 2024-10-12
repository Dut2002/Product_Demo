package com.example.demo_oracle_db.service.login.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginReq {
    @NotNull
    String username;
    @NotNull
    String password;
}
