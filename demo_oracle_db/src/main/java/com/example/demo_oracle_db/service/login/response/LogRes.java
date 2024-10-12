package com.example.demo_oracle_db.service.login.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LogRes {
    private String username;
    private String fullName;
    private String email;
    private String status;
    private String token;
    private String refreshToken;
    private List<String> roles;
}
