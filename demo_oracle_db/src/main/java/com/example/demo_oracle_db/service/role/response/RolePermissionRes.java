package com.example.demo_oracle_db.service.role.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RolePermissionRes {
    Long id;
    String name;
    List<FunctionDto> functions;
}
