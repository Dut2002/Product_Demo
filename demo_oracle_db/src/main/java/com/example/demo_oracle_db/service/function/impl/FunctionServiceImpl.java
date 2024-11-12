package com.example.demo_oracle_db.service.function.impl;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Permission;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.PermissionRepository;
import com.example.demo_oracle_db.repository.RolePermissionRepository;
import com.example.demo_oracle_db.service.function.FunctionService;
import com.example.demo_oracle_db.service.function.request.AddFunctionRequest;
import com.example.demo_oracle_db.service.function.request.AddPermissionRequest;
import com.example.demo_oracle_db.service.function.request.UpdateFunctionRequest;
import com.example.demo_oracle_db.service.function.request.UpdatePermissionRequest;
import com.example.demo_oracle_db.service.role.response.DeletePermissionRes;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.PermissionDto;
import com.example.demo_oracle_db.util.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, request.getName());
        }
        if (functionRepository.existsByFunctionName(request.getName())) {
            throw new DodException(MessageCode.FUNCTION_FE_ROUTE_EXISTS, request.getFeRoute());
        }
        functionRepository.addFunction(
                request.getName(),
                request.getFeRoute());
        Long functionId = functionRepository.getLastInsertedId();
        permissionRepository.addPermission(
                "View " + request.getName(),
                "/api/" + request.getFeRoute(),
                Long.valueOf(functionId),
                1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFunction(UpdateFunctionRequest req) throws DodException {
        if (!functionRepository.existsById(req.getId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        if (functionRepository.existsByFunctionNameAndIdNot(req.getName(), req.getId())) {
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, req.getName());
        }
        functionRepository.updateFunction(req.getId(), req.getName(), req.getFeRoute());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunction(Long id) throws DodException {
        if (!functionRepository.existsById(id)) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        List<Long> permissionIds = permissionRepository.getIdByFunctionId(id);
        for (Long permissionId : permissionIds
        ) {
            rolePermissionRepository.deleteByPermissionId(permissionId);
        }
        permissionRepository.deleteByFunctionId(id);
        functionRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(AddPermissionRequest request) throws DodException {
        if (!functionRepository.existsById(request.getFunctionId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        if (permissionRepository.existsByNameAndFunctionId(request.getName(), request.getFunctionId())) {
            throw new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        if (permissionRepository.existsByBeEndPoint(request.getBeEndPoint())) {
            throw new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        permissionRepository.addPermission(
                request.getName(),
                request.getBeEndPoint(),
                request.getFunctionId(),
                request.getDefaultPermission() != null && request.getDefaultPermission() ? 1 : 0);
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
        permissionRepository.updatePermission(
                request.getId(),
                request.getName(),
                request.getBeEndPoint(),
                request.getDefaultPermission() ? 1 : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeletePermissionRes deletePermission(Long permissionId) throws DodException {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new DodException(MessageCode.PERMISSION_NOT_FOUND));
        DeletePermissionRes res = new DeletePermissionRes();
        if (permission.getDefaultPermission() == 1 && !permissionRepository
                .existsByIdNotAndFunctionIdAndDefaultPermission(permissionId, permission.getFunctionId(),
                        1)) {
            deleteFunction(permission.getFunctionId());
            res.setDeleteFunc(true);
            res.setMessage("Delete Functions Successfully!");
        } else {
            rolePermissionRepository.deleteByPermissionId(permissionId);
            permissionRepository.deleteById(permissionId);
            res.setDeleteFunc(false);
            res.setMessage("Delete Permission Successfully!");
        }
        return res;
    }

    @Override
    public List<FunctionDto> viewAll() {
        List<Function> functions = functionRepository.findAll((root, query, criteriaBuilder) -> null);
        return FunctionDto.mapByFunction(functions);
    }

    @Override
    public String checkDeletePermission(Long permissionId) throws DodException {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new DodException(MessageCode.PERMISSION_NOT_FOUND));
        String message = "";
        if (permission.getDefaultPermission() == 1) {
            if (permissionRepository.existsByIdNotAndFunctionIdAndDefaultPermission(permissionId, permission.getFunctionId(), 1)) {
                message += "This is the default permission in this function.";
            } else {
                message += "This is the last default permission in this function. " +
                        "Removing the permission will also remove the function. ";
            }
        }
        return message + " Are you sure you want to delete this permission?";
    }

    @Override
    public List<PermissionDto> getPermissions(Long functionId) {
        List<Permission> permissions = permissionRepository.findAllByFunctionId(functionId);
        return permissions.stream().map(PermissionDto::new).toList();
    }

}
