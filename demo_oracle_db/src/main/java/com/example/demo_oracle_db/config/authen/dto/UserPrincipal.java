package com.example.demo_oracle_db.config.authen.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@NoArgsConstructor
public class UserPrincipal implements UserDetails {
    String role;
    String password;
    String username;
    Set<GrantedAuthority> authorities;

    public UserPrincipal(String username, @Size(max = 255) String password, String role, Set<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
    }
}
