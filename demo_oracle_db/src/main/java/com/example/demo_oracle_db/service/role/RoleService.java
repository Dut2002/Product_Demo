package com.example.demo_oracle_db.service.role;

import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.role.request.RoleReq;

import java.util.List;

public interface RoleService
{
    List<Role> getAll();

    Role getById(Long id);

    Role getByName(String name);

    void addRole(RoleReq role) throws DodException;

    void updateRole(RoleReq role) throws DodException;

    void deleteRole(Long id) throws DodException;
}
