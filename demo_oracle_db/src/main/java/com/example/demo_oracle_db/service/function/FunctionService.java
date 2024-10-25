package com.example.demo_oracle_db.service.function;

import com.example.demo_oracle_db.config.authen.dto.FunctionInfo;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.function.request.*;
import com.example.demo_oracle_db.service.function.response.DeleteConfirm;

import java.util.List;

public interface FunctionService {
    void addFunction(AddFunctionRequest functionName) throws DodException;

    void updateFunction(UpdateFunctionRequest req) throws DodException;

    void deleteFunction(Long id) throws DodException;

    void addPermission(AddPermissionRequest request) throws DodException;

    void updatePermission(UpdatePermissionRequest request) throws DodException;

    void deletePermission(DeletePermissionRequest id) throws DodException;

    List<FunctionInfo> viewAll();

    DeleteConfirm checkDeletePermission(Long permissionId) throws DodException;
}
