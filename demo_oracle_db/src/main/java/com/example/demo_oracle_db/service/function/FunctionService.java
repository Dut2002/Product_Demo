package com.example.demo_oracle_db.service.function;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.function.request.AddPermissionRequest;
import com.example.demo_oracle_db.service.function.request.FunctionRequest;
import com.example.demo_oracle_db.service.function.request.UpdatePermissionRequest;
import com.example.demo_oracle_db.service.function.response.FunctionResponse;
import com.example.demo_oracle_db.service.function.response.PermissionResponse;

import java.util.List;

public interface FunctionService {
    List<FunctionResponse> getAll();

    void addFunction(FunctionRequest functionName) throws DodException;

    void updateFunction(FunctionRequest req) throws DodException;

    void deleteFunction(Long id) throws DodException;

    List<PermissionResponse> getAllPermissionDetail(Long functionId) throws DodException;

    PermissionResponse getPermissionDetail(Long id) throws DodException;

    void addPermission(AddPermissionRequest request) throws DodException;

    void updatePermission(UpdatePermissionRequest request) throws DodException;

    void deletePermission(Long id) throws DodException;
}
