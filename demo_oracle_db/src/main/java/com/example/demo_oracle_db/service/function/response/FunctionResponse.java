package com.example.demo_oracle_db.service.function.response;

import com.example.demo_oracle_db.entity.Function;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FunctionResponse {
    Long id;
    String name;
    List<PermissionResponse> permissions;

    public FunctionResponse(Function function) {
        this.id = function.getId();
        this.name = function.getFunctionName();
        this.permissions = function.getPermissions().stream().map(PermissionResponse::new).toList();
    }
}
