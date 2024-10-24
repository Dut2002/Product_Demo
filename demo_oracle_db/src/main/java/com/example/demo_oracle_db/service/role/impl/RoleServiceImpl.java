package com.example.demo_oracle_db.service.role.impl;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.PermissionRepository;
import com.example.demo_oracle_db.repository.RolePermissionRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.service.role.RoleService;
import com.example.demo_oracle_db.service.role.request.ChangePermissionRequest;
import com.example.demo_oracle_db.service.role.request.RoleReq;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.PermissionDto;
import com.example.demo_oracle_db.service.role.response.RoleRes;
import com.example.demo_oracle_db.util.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    roleRes.setFunctions(getRolePermission(role.getId()));
                    return roleRes;
                }
        ).toList();
    }

    @Override
    public void addRole(RoleReq req) throws DodException {
        if (roleRepository.existsByName(req.getName())) {
            throw new DodException(MessageCode.ROLE_NAME_EXIST, req.getName());
        }
        Role role = new Role();
        role.setName("ROLE_" + req.getName().toUpperCase().replace(" ", "_"));
        roleRepository.save(role);
    }

    @Override
    public void updateRole(RoleReq req) throws DodException {
        Role role = roleRepository.findById(req.getId()).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));

        if (roleRepository.existsByNameAndIdNot(req.getName(), req.getId())) {
            throw new DodException(MessageCode.ROLE_NAME_EXIST, role.getName());
        }
        role.setName(req.getName());
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) throws DodException {
        if (!roleRepository.existsById(id)) {
            throw new DodException(MessageCode.ROLE_NOT_EXIST, id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    public List<FunctionDto> getRolePermission(Long id) {
        List<PermissionDto> permissions = permissionRepository.findPermissionsByRoleId(id).stream().map(PermissionDto::new).toList();
        List<Function> functions = functionRepository.findAll((root, query, criteriaBuilder) -> null);
        return FunctionDto.mapToFunctionDto(functions, permissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePermission(ChangePermissionRequest request) throws DodException {
//        if (!roleRepository.existsById(request.getRoleId())) {
//            throw new DodException(MessageCode.ROLE_NOT_FOUND);
//        }
//        List<RolePermission> rolePermissions = new ArrayList<>();
//        for (PermissionRequest permission: request.getPermissions()
//             ) {
//            if (!permissionRepository.existsById(permission.getId())) {
//                throw new DodException(MessageCode.PERMISSION_NOT_FOUND);
//            }
//            RolePermission rolePermission = rolePermissionRepository
//                    .findByPermissionIdAndRoleId(permission.getId(), request.getRoleId())
//                    .orElse(new RolePermission(permission.getId(), request.getRoleId()));
//
//            rolePermission.setStatus(permission.getStatus());
//            rolePermissions.add(rolePermission);
//        }
//        rolePermissionRepository.saveAll(rolePermissions);
    }
}

