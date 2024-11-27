package com.example.demo_oracle_db.service.function;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.function.request.AddFunctionRequest;
import com.example.demo_oracle_db.service.function.request.AddPermissionRequest;
import com.example.demo_oracle_db.service.function.request.UpdateFunctionRequest;
import com.example.demo_oracle_db.service.function.request.UpdatePermissionRequest;
import com.example.demo_oracle_db.service.role.response.DeletePermissionRes;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.PermissionDto;

import java.util.List;

public interface FunctionService {
    void addFunction(AddFunctionRequest functionName) throws DodException;

    void updateFunction(UpdateFunctionRequest req) throws DodException;

    void deleteFunction(Long id) throws DodException;

    void addPermission(AddPermissionRequest request) throws DodException;

    void updatePermission(UpdatePermissionRequest request) throws DodException;

    DeletePermissionRes deletePermission(Long id) throws DodException;

    List<FunctionDto> viewAll() throws DodException;

    String checkDeletePermission(Long permissionId) throws DodException;

    List<PermissionDto> getPermissions(Long functionId);
}
