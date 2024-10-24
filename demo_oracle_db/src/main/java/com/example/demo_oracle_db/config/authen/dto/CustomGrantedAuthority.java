package com.example.demo_oracle_db.config.authen.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class CustomGrantedAuthority implements GrantedAuthority {

    private final FunctionInfo functionInfo;

    public CustomGrantedAuthority(FunctionInfo functionInfo) {
        this.functionInfo = new FunctionInfo(functionInfo.getName(), functionInfo.getFeRoute(), functionInfo.getPermissions());
    }

    @Override
    public String getAuthority() {
        return this.functionInfo.getName();
    }

}
