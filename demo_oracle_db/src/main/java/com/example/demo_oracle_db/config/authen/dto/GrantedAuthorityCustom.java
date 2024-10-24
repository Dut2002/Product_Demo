package com.example.demo_oracle_db.config.authen.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class GrantedAuthorityCustom implements GrantedAuthority {

    // Getter cho PermissionInfo để truy cập thông tin khác nếu cần
    private final FunctionInfo functionInfo;

    // Constructor để khởi tạo PermissionInfo
    public GrantedAuthorityCustom(FunctionInfo functionInfo) {
        this.functionInfo = functionInfo;
    }

    @Override
    public String getAuthority() {
        // Trả về tên quyền từ PermissionInfo
        return functionInfo.getName();
    }

}

