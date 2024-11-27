package com.example.demo_oracle_db.service.role;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.role.request.RoleReq;
import com.example.demo_oracle_db.service.role.response.RoleRes;

import java.util.List;

public interface RoleService {
    List<RoleRes> getAll() throws DodException;

    void addRole(RoleReq role) throws DodException;

    void updateRole(RoleReq role) throws DodException;

    void deleteRole(Long id) throws DodException;
}
