package com.example.demo_oracle_db.service.role.response;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        name = func.getFunctionName();
        feRoute = func.getFeRoute();
        permissions = func.getPermissions().stream().map(PermissionDto::new).toList();
    }

    public static List<FunctionDto> mapByPermission(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) return new ArrayList<>();
        Map<Function, List<Permission>> listMap = permissions.stream()
                .collect(Collectors.groupingBy(Permission::getFunction));
        return listMap.entrySet().stream().map(entry -> {
            Function func = entry.getKey();
            List<Permission> permissionsList = entry.getValue();
            return new FunctionDto(
                    func.getId(),
                    func.getFunctionName(),
                    func.getFeRoute(),
                    permissionsList.stream().map(PermissionDto::new).toList()
            );
        }).toList();
    }
}
