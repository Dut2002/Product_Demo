package com.example.demo_oracle_db.service.fixSystem.impl;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Permission;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.*;
import com.example.demo_oracle_db.service.fixSystem.FixSystemService;
import com.example.demo_oracle_db.service.fixSystem.request.AddSuperAdminFunction;
import com.example.demo_oracle_db.service.fixSystem.request.AddSuperAdminPermission;
import com.example.demo_oracle_db.service.function.request.AddFunctionRequest;
import com.example.demo_oracle_db.service.function.request.AddPermissionRequest;
import com.example.demo_oracle_db.service.priority.PriorityService;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import com.example.demo_oracle_db.service.role.response.FunctionDto;
import com.example.demo_oracle_db.service.role.response.PermissionDto;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FixSystemServiceImpl implements FixSystemService {
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
    @Autowired
    PriorityService priorityService;

    @Override
    public List<FunctionDto> viewFunction() throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));
        return getFunctionsByRoleId(roleId, priority);
    }

    private List<FunctionDto> getFunctionsByRoleId(Long roleId, Integer priority) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FunctionDto> query = cb.createQuery(FunctionDto.class);
        Root<Function> root = query.from(Function.class);
        query.multiselect(root.get("id"), root.get("name"), root.get("feRoute"));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.greaterThanOrEqualTo(root.get("priority"), priority));
        predicates.add(cb.equal(root.join("functionRoleList").get("roleId"), roleId));
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        List<FunctionDto> functions = entityManager.createQuery(query).getResultList();
        for (FunctionDto function : functions
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
    public void addFunction(AddSuperAdminFunction request) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkFunctionPriority(priority, request.getFunctionId());
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));
        //Add những permission required của function cho role
        if (!functionRepository.existsById(request.getFunctionId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        if (functionRoleRepository.existsByFunctionIdAndRoleId(request.getFunctionId(), roleId)) {
            throw new DodException(MessageCode.FUNCTION_ALREADY_ADD);
        }

        functionRoleRepository.addFunctionRole(request.getFunctionId(), roleId);

        List<Long> permissions = permissionRepository.getIdByFunctionIdAndDefaultPermission(request.getFunctionId(), 1);
        for (Long permission : permissions
        ) {
            rolePermissionRepository.addPermissionRole(permission, roleId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunction(Long functionId) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkFunctionPriority(priority, functionId);
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));
        if (!functionRepository.existsById(functionId)) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        if (!functionRoleRepository.existsByFunctionIdAndRoleId(functionId, roleId)) {
            throw new DodException(MessageCode.FUNCTION_ROLE_NOT_FOUND);
        }
        functionRoleRepository.deleteByFunctionIdAndRoleId(functionId, roleId);
        rolePermissionRepository.deleteByFunctionId(functionId, roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(AddSuperAdminPermission request) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));

        if (!permissionRepository.existsById(request.getPermissionId())) {
            throw new DodException(MessageCode.PERMISSION_NOT_FOUND);
        }
        Long functionId = permissionRepository.getFunctionIdById(request.getPermissionId());
        priorityService.checkFunctionPriority(priority, functionId);


        if (!functionRoleRepository.existsByFunctionIdAndRoleId(functionId, roleId)) {
            throw new DodException(MessageCode.FUNCTION_ROLE_NOT_FOUND);
        }
        if (rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, request.getPermissionId())) {
            throw new DodException(MessageCode.PERMISSION_ALREADY_ADD);
        }
        rolePermissionRepository.addPermissionRole(request.getPermissionId(), roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long permissionId) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));

        if (!permissionRepository.existsById(permissionId)) {
            throw new DodException(MessageCode.PERMISSION_NOT_FOUND);
        }

        Long functionId = permissionRepository.getFunctionIdById(permissionId);
        priorityService.checkFunctionPriority(priority, functionId);

        if (!functionRoleRepository.existsByFunctionIdAndRoleId(functionId, roleId)) {
            throw new DodException(MessageCode.FUNCTION_ROLE_NOT_FOUND);
        }

        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            throw new DodException(MessageCode.ROLE_PERMISSION_NOT_FOUND);
        }
        if (permissionRepository.existsByIdAndDefaultPermission(permissionId, 1)) {
            throw new DodException(MessageCode.PERMISSION_DEFAULT);
        }
        rolePermissionRepository.deleteByRoleIdAndPermissionId(roleId, permissionId);
    }

    @Override
    public List<SearchBox> getFunctionSearch() throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));
        return functionRepository.getFunctionRoleNot(roleId, priority).stream().map(SearchBox::new).toList();
    }

    @Override
    public List<SearchBox> getPermissionSearch(Long functionId) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));
        priorityService.checkFunctionPriority(priority, functionId);
        return permissionRepository.getPermissionRoleNot(roleId, functionId).stream().map(SearchBox::new).toList();
    }

    @Override
    public List<PermissionDto> getUserFunctionPermission(Long functionId) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkFunctionPriority(priority, functionId);

        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));
        if (!functionRoleRepository.existsByFunctionIdAndRoleId(functionId, roleId)) {
            throw new DodException(MessageCode.FUNCTION_ROLE_NOT_FOUND);
        }
        List<Permission> permissions = permissionRepository.getUserFunctionPermission(roleId, functionId);
        return permissions.stream().map(PermissionDto::new).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNewFunction(AddFunctionRequest request) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));

        if (functionRepository.existsByName(request.getName())) {
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, request.getName());
        }
        if (functionRepository.existsByName(request.getName())) {
            throw new DodException(MessageCode.FUNCTION_FE_ROUTE_EXISTS, request.getFeRoute());
        }
        functionRepository.addFunction(
                request.getName(),
                request.getFeRoute(),
                priority);
        Long functionId = functionRepository.getLastInsertedId();
        functionRoleRepository.addFunctionRole(functionId, roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNewPermission(AddPermissionRequest request) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkFunctionPriority(priority, request.getFunctionId());
        Long roleId = roleRepository.findIdByName(Constants.Role.SUPER_ADMIN).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));

        if (!functionRepository.existsById(request.getFunctionId())) {
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        if (permissionRepository.existsByNameAndFunctionId(request.getName(), request.getFunctionId())) {
            throw new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }
        if (permissionRepository.existsByBeEndPoint(request.getBeEndPoint())) {
            throw new DodException(MessageCode.PERMISSION_NAME_EXISTS, request.getName());
        }

        Integer isDefault = request.getDefaultPermission() != null && request.getDefaultPermission() ? 1 : 0;
        permissionRepository.addPermission(
                request.getName(),
                request.getBeEndPoint(),
                request.getFunctionId(),
                isDefault
        );
        Long permissionId = permissionRepository.getLastInsertedId();
        if (isDefault.equals(1)) {
            List<Long> roles = rolePermissionRepository.findRoleWithFuctionId(request.getFunctionId());
            for (Long role : roles
            ) {
                rolePermissionRepository.addPermissionRole(permissionId, role);
            }
        }
        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(permissionId, roleId)) {
            rolePermissionRepository.addPermissionRole(permissionId, roleId);

        }

    }
}
