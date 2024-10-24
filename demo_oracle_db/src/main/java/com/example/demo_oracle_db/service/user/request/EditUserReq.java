package com.example.demo_oracle_db.service.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUserReq {

    @NotNull
    Long id;
    @NotBlank
    String username;
    @Email
    String email;
    @NotBlank
    String fullName;
}
