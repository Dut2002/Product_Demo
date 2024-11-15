package com.example.demo_oracle_db.config.authen.dto;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Permission;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class FunctionInfo {
    String name;
    String feRoute;
    List<PermissionInfo> permissions;

    public static List<FunctionInfo> mapToFunctionInfo(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) return new ArrayList<>();
        Map<Function, List<Permission>> listMap = permissions.stream()
                .collect(Collectors.groupingBy(Permission::getFunction));
        return listMap.entrySet().stream().map(entry -> {
            Function func = entry.getKey();
            List<Permission> permissionsList = entry.getValue();
            return new FunctionInfo(
                    func.getName(),
                    func.getFeRoute(),
                    permissionsList.stream().map(PermissionInfo::new).toList()
            );
        }).toList();
    }

    @JsonCreator
    public FunctionInfo(@JsonProperty("name") String name, @JsonProperty("feRoute") String feRoute, @JsonProperty("permissions") List<PermissionInfo> permissions) {
        this.name = name;
        this.feRoute = feRoute;
        this.permissions = permissions;
    }
}
