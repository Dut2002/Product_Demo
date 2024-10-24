package com.example.demo_oracle_db.service.login.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisReq {

    @NotBlank(message = "Username is required")
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 255)
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 255)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Size(max = 100)
    @Email(message = "Email format invalid")
    private String email;
}
