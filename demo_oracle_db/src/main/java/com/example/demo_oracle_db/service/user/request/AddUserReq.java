package com.example.demo_oracle_db.service.user.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddUserReq {
    @NotBlank(message = "Username is required")
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @NotBlank(message = "Full name is required")
    @Size(max = 255)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Size(max = 100)
    @Email(message = "Email format invalid")
    private String email;

    @NotNull(message = "Role is required")
    @Size(min = 1)
    private Long[] roles;
}
