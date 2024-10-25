package com.example.demo_oracle_db.service.function.impl;

import com.example.demo_oracle_db.config.authen.dto.FunctionInfo;
import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Permission;
import com.example.demo_oracle_db.entity.RolePermission;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.PermissionRepository;
import com.example.demo_oracle_db.repository.RolePermissionRepository;
import com.example.demo_oracle_db.service.function.FunctionService;
import com.example.demo_oracle_db.service.function.request.*;
import com.example.demo_oracle_db.service.function.response.DeleteConfirm;
import com.example.demo_oracle_db.util.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
class FunctionServiceImpl implements FunctionService {
    @Autowired
    FunctionRepository functionRepository;
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFunction(AddFunctionRequest request) throws DodException {
        if (functionRepository.existsByFunctionName(request.getName())) {
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, request);
        }
        if (functionRepository.existsByFunctionName(request.getName())) {
            throw new DodException(MessageCode.FUNCTION_FE_ROUTE_EXISTS, request);
        }
        Function function = new Function();
        function.setFunctionName(request.getName());
        function = functionRepository.save(function);

        Permission permission = new Permission();
        permission.setName("View " + function.getFunctionName());
        permission.setBeEndPoint("/api/" + function.getFeRoute());
        permission.setFunctionId(function.getId());
        permission.setDefaultPermission(1);
        permissionRepository.save(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFunction(UpdateFunctionRequest req) throws DodException {
        Function function = functionRepository.findById(req.getId()).orElseThrow(() -> new DodException(MessageCode.FUNCTION_NOT_FOUND));
        if (functionRepository.existsByFunctionNameAndIdNot(req.getName(), req.getId())) {
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, req.getName());
        }
        function.setFunctionName(req.getName());
        functionRepository.save(function);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunction(Long id) throws DodException {
        if (!functionRepository.existsById(id)) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        List<Permission> permissions = permissionRepository.findAllByFunctionId(id);
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (Permission permission : permissions
        ) {
            rolePermissions.addAll(permission.getRolePermissions());
        }
        rolePermissionRepository.deleteAll(rolePermissions);
        permissionRepository.deleteAll(permissions);
        functionRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(AddPermissionRequest request) throws DodException {
        if (!functionRepository.existsById(request.getFunctionId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        if (permissionRepository.existsByName(request.getName())) {
            throw new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        if (permissionRepository.existsByBeEndPoint(request.getBeEndPoint())) {
            throw new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        Permission permission = new Permission();
        permission.setFunctionId(request.getFunctionId());
        permission.setName(request.getName());
        permission.setBeEndPoint(request.getBeEndPoint());
        permissionRepository.save(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(UpdatePermissionRequest request) throws DodException {
        Permission permission = permissionRepository.findById(request.getId()).orElseThrow(() -> new DodException(MessageCode.PERMISSION_NOT_FOUND));
        if (permissionRepository.existsByNameAndFunctionIdAndIdNot(request.getName(), permission.getFunctionId(), request.getId())) {
            throw new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        if (permissionRepository.existsByBeEndPointAndFunctionIdAndIdNot(request.getBeEndPoint(), permission.getFunctionId(), request.getId())) {
            throw new DodException(MessageCode.PERMISSION_BE_ENDPOINT_EXISTS, request.getName());
        }
        permission.setName(request.getName());
        permission.setBeEndPoint(request.getBeEndPoint());
        permissionRepository.save(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(DeletePermissionRequest request) throws DodException {
        Permission permission = permissionRepository.findById(request.getId())
                .orElseThrow(() -> new DodException(MessageCode.PERMISSION_NOT_FOUND));
        if (permission.getDefaultPermission() == 1) {
            throw new DodException(MessageCode.PERMISSION_REQUIRED);
        }
        List<RolePermission> rolePermissions = permission.getRolePermissions();
        rolePermissionRepository.deleteAll(rolePermissions);
        permissionRepository.deleteById(request.getId());
    }

    @Override
    public List<FunctionInfo> viewAll() {
        List<Permission> permissions = permissionRepository.findAll((root, query, criteriaBuilder) -> null);
        return FunctionInfo.mapToFunctionInfo(permissions);
    }

    @Override
    public DeleteConfirm checkDeletePermission(Long permissionId) throws DodException {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new DodException(MessageCode.PERMISSION_NOT_FOUND));

        DeleteConfirm response = new DeleteConfirm();
        response.setPermissionId(permission.getId());

        if (permission.getDefaultPermission() == 1) {
            List<Permission> permissions = permissionRepository
                    .findAllByFunctionIdAndDefaultPermission(permission.getFunctionId(), 1);

            if (permissions.size() > 1) {
                response.setNeedConfirm(true);
                response.setMessage("Are you sure you want to remove the default permissions in this function?");
            } else if (permissions.size() == 1) {
                response.setNeedConfirm(true);
                response.setMessage("This is the last default permission in this function. " +
                        "Removing the permission will also remove the function. " +
                        "Are you sure you want to delete?");
            }
        }
        return response;
    }

}
