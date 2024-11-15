package com.example.demo_oracle_db.service.role.impl;

import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.PermissionRepository;
import com.example.demo_oracle_db.repository.RolePermissionRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.service.role.RoleService;
import com.example.demo_oracle_db.service.role.request.RoleReq;
import com.example.demo_oracle_db.service.role.response.RoleRes;
import com.example.demo_oracle_db.util.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    FunctionRepository functionRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Override
    public List<RoleRes> getAll() {
        List<Role> roles = roleRepository.findAll((root, query, criteriaBuilder) -> null);
        return roles.stream().map(role -> {
                    RoleRes roleRes = new RoleRes();
                    roleRes.setId(role.getId());
                    roleRes.setName(role.getName().replace("ROLE_", "").replace("_", " "));
                    return roleRes;
                }
        ).toList();
    }

    @Override
    public void addRole(RoleReq req) throws DodException {
        if (roleRepository.existsByName(req.getName())) {
            throw new DodException(MessageCode.ROLE_NAME_EXIST, req.getName());
        }
        roleRepository.addRole("ROLE_" + req.getName().toUpperCase().replace(" ", "_"));
    }

    @Override
    public void updateRole(RoleReq req) throws DodException {

        if(!roleRepository.existsById(req.getId())){
            throw new DodException(MessageCode.ROLE_NOT_FOUND);
        }

        if (roleRepository.existsByNameAndIdNot(req.getName(), req.getId())) {
            throw new DodException(MessageCode.ROLE_NAME_EXIST, req.getName());
        }
        roleRepository.updateRole(req.getId(), "ROLE_" + req.getName().toUpperCase().replace(" ", "_"));
    }

    @Override
    public void deleteRole(Long id) throws DodException {
        if (!roleRepository.existsById(id)) {
            throw new DodException(MessageCode.ROLE_NOT_EXIST);
        }
        roleRepository.deleteById(id);
    }
}

