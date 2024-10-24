package com.example.demo_oracle_db.service.role;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.role.request.ChangePermissionRequest;
import com.example.demo_oracle_db.service.role.request.RoleReq;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.RoleRes;

import java.util.List;

public interface RoleService
{
    List<RoleRes> getAll();

    void addRole(RoleReq role) throws DodException;

    void updateRole(RoleReq role) throws DodException;

    void deleteRole(Long id) throws DodException;

    List<FunctionDto> getRolePermission(Long id);

    void changePermission(ChangePermissionRequest request) throws DodException;
}
