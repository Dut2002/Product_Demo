package com.example.demo_oracle_db.service.login.response;

import com.example.demo_oracle_db.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LogRes {
    private String username;
    private String fullName;
    private String email;
    private Constants.Status status;
    private String token;
    private String refreshToken;
    private List<String> roles;
}
