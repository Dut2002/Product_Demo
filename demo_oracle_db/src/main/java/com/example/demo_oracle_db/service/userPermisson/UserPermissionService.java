package com.example.demo_oracle_db.service.userPermisson;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.RolePermissionRes;
import com.example.demo_oracle_db.service.userPermisson.request.*;

import java.util.List;

public interface UserPermissionService {
    void addPermission(AddUserPermissionRequest request) throws DodException;

    void addNewPermission(AddNewUserPermissionRequest request) throws DodException;

    void deleteUserPermission(DeleteUserPermissionRequest request) throws DodException;

    RolePermissionRes getPermissionsByRole(Long roleId) throws DodException;

    void addUserFunction(AddUserFunction request) throws DodException;

    void addUserNewFunction(AddUserNewFunction request) throws DodException;

    void deleteUserFunction(DeleteUserFunctionRequest request) throws DodException;
}
