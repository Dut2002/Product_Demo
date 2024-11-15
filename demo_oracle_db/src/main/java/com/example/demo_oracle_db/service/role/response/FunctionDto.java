package com.example.demo_oracle_db.service.role.response;

import com.example.demo_oracle_db.entity.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FunctionDto {
    Long id;
    String name;
    String feRoute;
    List<PermissionDto> permissions;

    public static List<FunctionDto> mapByFunction(List<Function> functions){
        if(functions == null || functions.isEmpty()) return new ArrayList<>();
        return functions.stream().map(FunctionDto::new).toList();
    }

    public FunctionDto (Function func){
        id = func.getId();
        name = func.getName();
        feRoute = func.getFeRoute();
        permissions = func.getPermissions().stream().map(PermissionDto::new).toList();
    }

    public FunctionDto(Long id, String name, String feRoute) {
        this.id = id;
        this.name = name;
        this.feRoute = feRoute;
    }
}
