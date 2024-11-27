package com.example.demo_oracle_db.service.fixSystem;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.fixSystem.request.AddSuperAdminFunction;
import com.example.demo_oracle_db.service.fixSystem.request.AddSuperAdminPermission;
import com.example.demo_oracle_db.service.function.request.AddFunctionRequest;
import com.example.demo_oracle_db.service.function.request.AddPermissionRequest;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.PermissionDto;

import java.util.List;

public interface FixSystemService {
    List<FunctionDto> viewFunction() throws DodException;

    void addFunction(AddSuperAdminFunction request) throws DodException;

    void deleteFunction(Long functionId) throws DodException;

    void addPermission(AddSuperAdminPermission request) throws DodException;

    void deletePermission(Long permissionId) throws DodException;

    List<SearchBox> getFunctionSearch() throws DodException;

    List<SearchBox> getPermissionSearch(Long functionId) throws DodException;

    List<PermissionDto> getUserFunctionPermission(Long functionId) throws DodException;

    void addNewFunction(AddFunctionRequest request) throws DodException;

    void addNewPermission(AddPermissionRequest request) throws DodException;
}
