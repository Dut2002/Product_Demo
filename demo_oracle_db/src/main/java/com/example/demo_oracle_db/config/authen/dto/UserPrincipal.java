package com.example.demo_oracle_db.config.authen.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
public class UserPrincipal implements UserDetails {
    List<String> roles;
    String password;
    String username;
    Set<GrantedAuthorityCustom> authorities;

    public UserPrincipal(String username, @Size(max = 255) String password, List<String> roles, Set<GrantedAuthorityCustom> authorities) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authorities = authorities;
    }
}
