package com.example.demo_oracle_db.service.userPermisson.impl;

import com.example.demo_oracle_db.entity.Permission;
import com.example.demo_oracle_db.entity.RolePermission;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.PermissionRepository;
import com.example.demo_oracle_db.repository.RolePermissionRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.RolePermissionRes;
import com.example.demo_oracle_db.service.userPermisson.UserPermissionService;
import com.example.demo_oracle_db.service.userPermisson.request.*;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPermissionServiceImpl implements UserPermissionService {
    @Autowired
    FunctionRepository functionRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RolePermissionRepository rolePermissionRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public RolePermissionRes getPermissionsByRole(Long roleId) throws DodException {
        String roleName = roleRepository.findNameById(roleId).orElseThrow(()->new DodException(MessageCode.ROLE_NOT_FOUND));
        List<Permission> permissions = permissionRepository.findPermissionsByRole(List.of(roleId));
        List<FunctionDto> functions = FunctionDto.mapByPermission(permissions);
        RolePermissionRes res = new RolePermissionRes();
        res.setId(roleId);
        res.setName(roleName);
        res.setFunctions(functions);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserFunction(AddUserFunction request) throws DodException {
        if (!roleRepository.existsById(request.getRoleId())) {
            throw new DodException(MessageCode.ROLE_NOT_FOUND);
        }
        //Add những permission required của function cho role
        if (!functionRepository.existsById(request.getFunctionId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }

        if (rolePermissionRepository.exists((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("roleId"), request.getRoleId()));
            predicates.add(criteriaBuilder.equal(root.join("permission", JoinType.INNER).get("functionId"), request.getFunctionId()));
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        })) {
            throw new DodException(MessageCode.FUNCTION_ALREADY_ADD);
        }

        List<Permission> permissions = permissionRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("functionId"), request.getFunctionId()));
            predicates.add(criteriaBuilder.equal(root.get("defaultPermission"), 1));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (Permission permission : permissions
        ) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permission.getId());
            rolePermission.setRoleId(request.getRoleId());
            rolePermissionList.add(rolePermission);
        }
        rolePermissionRepository.saveAll(rolePermissionList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserNewFunction(AddUserNewFunction request) throws DodException {
        if (!roleRepository.existsById(request.getRoleId())) {
            throw new DodException(MessageCode.ROLE_NOT_FOUND);
        }
        if (functionRepository.existsByFunctionName(request.getFunctionName())) {
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, request.getFunctionName());
        }
        if (functionRepository.existsByFeRoute(request.getFeRoute())) {
            throw new DodException(MessageCode.FUNCTION_FE_ROUTE_EXISTS, request.getFeRoute());
        }
        functionRepository.addFunction(request.getFunctionName(),request.getFeRoute());
        Long functionId = functionRepository.getLastInsertedId();
        permissionRepository.addPermission(
                "View " + request.getFunctionName(),
                "/api/" + request.getFeRoute().trim(),
                functionId,
                1
                );
        Long permissionId = permissionRepository.getLastInsertedId();
        rolePermissionRepository.addPermissionRole(permissionId, request.getRoleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserFunction(DeleteUserFunctionRequest request) throws DodException {
        if (!roleRepository.existsById(request.getRoleId())) {
            throw new DodException(MessageCode.ROLE_NOT_FOUND);
        }
        if (!functionRepository.existsById(request.getFunctionId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        rolePermissionRepository.deleteByFunctionId(request.getFunctionId(), request.getRoleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(AddUserPermissionRequest request) throws DodException {
        if (!roleRepository.existsById(request.getRoleId())) {
            throw new DodException(MessageCode.ROLE_NOT_FOUND);
        }
        if (!permissionRepository.existsById(request.getPermissionId())) {
            throw new DodException(MessageCode.PERMISSION_NOT_FOUND);
        }
        if (rolePermissionRepository.existsByRoleIdAndPermissionId(request.getRoleId(), request.getPermissionId())) {
            throw new DodException(MessageCode.PERMISSION_ALREADY_ADD);
        }
        rolePermissionRepository.addPermissionRole(request.getPermissionId(), request.getRoleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNewPermission(AddNewUserPermissionRequest request) throws DodException {
        if (!roleRepository.existsById(request.getRoleId())) {
            throw new DodException(MessageCode.ROLE_NOT_FOUND);
        }
        if (!functionRepository.existsById(request.getFunctionId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        if (permissionRepository.existsByNameAndFunctionId(request.getName(), request.getFunctionId())) {
            throw new DodException(MessageCode.PERMISSION_NAME_EXISTS);
        }
        if (permissionRepository.existsByBeEndPointAndFunctionId(request.getBeEndPoint(), request.getFunctionId())) {
            throw new DodException(MessageCode.PERMISSION_BE_ENDPOINT_EXISTS);
        }
        permissionRepository.addPermission(request.getName(), request.getBeEndPoint(), request.getFunctionId(), request.getDefaultPermission() ? 1 : 0);
        Long permissionId = permissionRepository.getLastInsertedId();
        rolePermissionRepository.addPermissionRole(permissionId, request.getRoleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserPermission(DeleteUserPermissionRequest request) throws DodException {
        if (!roleRepository.existsById(request.getRoleId())) {
            throw new DodException(MessageCode.ROLE_NOT_FOUND);
        }
        if (!functionRepository.existsById(request.getPermissionId())) {
            throw new DodException(MessageCode.PERMISSION_NOT_FOUND);
        }
        if(!rolePermissionRepository.existsByRoleIdAndPermissionId(request.getRoleId(), request.getPermissionId())){
            throw new DodException(MessageCode.ROLE_PERMISSION_NOT_FOUND);
        }
        rolePermissionRepository.deleteByRoleIdAndPermissionId(request.getRoleId(),request.getPermissionId());
    }
}
