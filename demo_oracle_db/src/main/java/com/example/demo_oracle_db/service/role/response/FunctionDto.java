package com.example.demo_oracle_db.service.role.response;

import com.example.demo_oracle_db.entity.Function;
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
    List<PermissionDto> permissions;

    public static List<FunctionDto> mapToFunctionDto(List<Function> functions, List<PermissionDto> permissions) {
        // Bước 1: Nhóm danh sách PermissionDto theo functionId
        Map<Long, List<PermissionDto>> permissionsByFunctionId = permissions.stream()
                .collect(Collectors.groupingBy(PermissionDto::getFunctionId));

        // Bước 2: Duyệt qua danh sách functions và map sang FunctionDto
        return functions.stream()
                .map(function -> {
                    List<PermissionDto> functionPermissions = permissionsByFunctionId.getOrDefault(function.getId(), new ArrayList<>());
                    return new FunctionDto(function.getId(), function.getFunctionName(), functionPermissions);
                })
                .collect(Collectors.toList());
    }
}
