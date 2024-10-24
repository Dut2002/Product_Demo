package com.example.demo_oracle_db.service.function.impl;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Permission;
import com.example.demo_oracle_db.entity.RolePermission;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.PermissionRepository;
import com.example.demo_oracle_db.repository.RolePermissionRepository;
import com.example.demo_oracle_db.service.function.FunctionService;
import com.example.demo_oracle_db.service.function.request.AddPermissionRequest;
import com.example.demo_oracle_db.service.function.request.FunctionRequest;
import com.example.demo_oracle_db.service.function.request.UpdatePermissionRequest;
import com.example.demo_oracle_db.service.function.response.FunctionResponse;
import com.example.demo_oracle_db.service.function.response.PermissionResponse;
import com.example.demo_oracle_db.util.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService {
    @Autowired
    FunctionRepository functionRepository;
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Override
    public List<FunctionResponse> getAll() {
        return functionRepository.findAll((root, query, criteriaBuilder) -> null).stream().map(FunctionResponse::new).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFunction(FunctionRequest request) throws DodException {
        if (functionRepository.existsByFunctionName(request.getName())) {
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, request);
        }
        Function function = new Function();
        function.setFunctionName(request.getName());
        functionRepository.save(function);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFunction(FunctionRequest req) throws DodException {
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
        for (Permission permission: permissions
             ) {
            List<RolePermission> rolePermissions = rolePermissionRepository.findAllByPermissionId(permission.getId());
            rolePermissionRepository.deleteAll(rolePermissions);
        }
        permissionRepository.deleteAll(permissions);
        functionRepository.deleteById(id);
    }

    @Override
    public List<PermissionResponse> getAllPermissionDetail(Long functionId) throws DodException {
        Function function = functionRepository.findById(functionId).orElseThrow(()->new DodException(MessageCode.FUNCTION_NOT_FOUND));
        return function.getPermissions().stream().map(PermissionResponse::new).toList();
    }

    @Override
    public PermissionResponse getPermissionDetail(Long id) throws DodException {
        return new PermissionResponse(permissionRepository.findById(id).orElseThrow(()->new DodException(MessageCode.PERMISSION_NOT_FOUND)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(AddPermissionRequest request) throws DodException {
        if (!functionRepository.existsById(request.getFunctionId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        if(permissionRepository.existsByName(request.getName())){
            throw  new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        if(permissionRepository.existsByBeEndPoint(request.getBeEndPoint())){
            throw  new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
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
        if(permissionRepository.existsByNameAndIdNot(request.getName(), request.getId())){
            throw  new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        if(permissionRepository.existsByBeEndPointAndIdNot(request.getBeEndPoint(), request.getId())){
            throw  new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        permission.setName(request.getName());
        permission.setBeEndPoint(request.getBeEndPoint());
        permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Long id) throws DodException {
        if (!permissionRepository.existsById(id)) {
            throw new DodException(MessageCode.PERMISSION_NOT_FOUND);
        }
        List<RolePermission> rolePermissions = rolePermissionRepository.findAllByPermissionId(id);
        rolePermissionRepository.deleteAll(rolePermissions);
        permissionRepository.deleteById(id);
    }
}
