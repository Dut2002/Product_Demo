package com.example.demo_oracle_db.config.authen.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserPrincipal implements UserDetails, Authentication  {
    List<String> roles;
    String password;
    String username;
    String fullName;
    Integer priority;
    Set<GrantedAuthorityCustom> authorities;
    private boolean authenticated = true;

    public UserPrincipal(String username, String fullName, @Size(max = 255) String password, Integer priority, List<String> roles, Set<GrantedAuthorityCustom> authorities) {
        this.username = username;
        this.password = password;
        this.priority = priority;
        this.fullName = fullName;
        this.roles = roles;
        this.authorities = authorities;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
