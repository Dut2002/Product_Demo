package com.example.demo_oracle_db.service.userPermisson.impl;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Permission;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.*;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.PermissionDto;
import com.example.demo_oracle_db.service.role.response.RolePermissionRes;
import com.example.demo_oracle_db.service.userPermisson.UserPermissionService;
import com.example.demo_oracle_db.service.userPermisson.request.*;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    FunctionRoleRepository functionRoleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EntityManager entityManager;

    @Override
    public RolePermissionRes getPermissionsByRole(Long roleId) throws DodException {
        String roleName = roleRepository.findNameById(roleId).orElseThrow(()->new DodException(MessageCode.ROLE_NOT_FOUND));
        List<FunctionDto> functions = getFunctionsByRoleId(roleId);
        RolePermissionRes res = new RolePermissionRes();
        res.setId(roleId);
        res.setName(roleName.replace("ROLE_", "").replace("_", " "));
        res.setFunctions(functions);
        return res;
    }

    private List<FunctionDto> getFunctionsByRoleId(Long roleId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FunctionDto> query = cb.createQuery(FunctionDto.class);
        Root<Function> root = query.from(Function.class);
        query.multiselect(root.get("id"), root.get("name"), root.get("feRoute"));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.join("functionRoleList").get("roleId"), roleId));
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        List<FunctionDto> functions = entityManager.createQuery(query).getResultList();
        for (FunctionDto function: functions
             ) {
            function.setPermissions(getPermissionsByRoleId(roleId, function.getId()));
        }
        return functions;
    }

    private List<PermissionDto> getPermissionsByRoleId(Long roleId, Long functionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PermissionDto> query = cb.createQuery(PermissionDto.class);
        Root<Permission> root = query.from(Permission.class);
        query.multiselect(root.get("id"), root.get("name"), root.get("beEndPoint"), root.get("defaultPermission"));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.and(
                cb.equal(root.get("functionId"), functionId),
                cb.equal(root.join("rolePermissionList").get("roleId"), roleId))
        );
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
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
        if(functionRoleRepository.existsByFunctionIdAndRoleId(request.getFunctionId(), request.getRoleId())){
            throw new DodException(MessageCode.FUNCTION_ALREADY_ADD);
        }

        functionRoleRepository.addFunctionRole(request.getFunctionId(), request.getRoleId());

        List<Long> permissions = permissionRepository.getIdByFunctionIdAndDefaultPermission(request.getFunctionId(), 1);
        for (Long permission : permissions
        ) {
            rolePermissionRepository.addPermissionRole(permission, request.getRoleId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserNewFunction(AddUserNewFunction request) throws DodException {
        if (!roleRepository.existsById(request.getRoleId())) {
            throw new DodException(MessageCode.ROLE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (functionRepository.existsByName(request.getFunctionName())) {
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
        if(!functionRoleRepository.existsByFunctionIdAndRoleId(request.getFunctionId(), request.getRoleId())){
            throw new DodException(MessageCode.FUNCTION_ROLE_NOT_FOUND);
        }
        functionRoleRepository.deleteByFunctionIdAndRoleId(request.getFunctionId(),request.getRoleId());
        rolePermissionRepository.deleteByFunctionId(request.getFunctionId(), request.getRoleId());
    }

    @Override
    public List<SearchBox> getFunctionSearch(Long id) throws DodException {
        if(!roleRepository.existsById(id)) throw new DodException(MessageCode.ROLE_NOT_FOUND);
        return functionRepository.getFunctionRoleNot(id).stream().map(SearchBox::new).toList();
    }

    @Override
    public List<SearchBox> getPermissionSearch(Long roleId, Long functionId) throws DodException {
        if(!roleRepository.existsById(roleId)) throw new DodException(MessageCode.ROLE_NOT_FOUND);
        return permissionRepository.getPermissionRoleNot(roleId, functionId).stream().map(SearchBox::new).toList();
    }

    @Override
    public List<PermissionDto> getUserFunctionPermission(Long roleId, Long functionId) throws DodException {
        if(!roleRepository.existsById(roleId)) throw new DodException(MessageCode.ROLE_NOT_FOUND);
        if(!functionRoleRepository.existsByFunctionIdAndRoleId(functionId, roleId)){
            throw new DodException(MessageCode.FUNCTION_ROLE_NOT_FOUND);
        }
        List<Permission> permissions = permissionRepository.getUserFunctionPermission(roleId, functionId);
        return permissions.stream().map(PermissionDto::new).toList();
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
        Long functionId = permissionRepository.getFunctionIdById(request.getPermissionId());
        if(!functionRoleRepository.existsByFunctionIdAndRoleId(functionId, request.getRoleId())){
            throw new DodException(MessageCode.FUNCTION_ROLE_NOT_FOUND);
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
        if (!permissionRepository.existsById(request.getPermissionId())) {
            throw new DodException(MessageCode.PERMISSION_NOT_FOUND);
        }
        if(!rolePermissionRepository.existsByRoleIdAndPermissionId(request.getRoleId(), request.getPermissionId())){
            throw new DodException(MessageCode.ROLE_PERMISSION_NOT_FOUND);
        }
        if(permissionRepository.existsByIdAndDefaultPermission(request.getPermissionId(), 1)){
            throw new DodException(MessageCode.PERMISSION_DEFAULT);
        }
        rolePermissionRepository.deleteByRoleIdAndPermissionId(request.getRoleId(),request.getPermissionId());
    }
}
