package com.example.demo_oracle_db.service.role.impl;

import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.*;
import com.example.demo_oracle_db.service.priority.PriorityService;
import com.example.demo_oracle_db.service.role.RoleService;
import com.example.demo_oracle_db.service.role.request.RoleReq;
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
    @Autowired
    AccountRoleRepository accountRoleRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PriorityService priorityService;

    @Override
    public List<RoleRes> getAll() throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);

        List<Role> roles = roleRepository.findAll((root, query, criteriaBuilder) ->
            criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("priority"), priority))
        );

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
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);

        if (roleRepository.existsByName(req.getName())) {
            throw new DodException(MessageCode.ROLE_NAME_EXIST, req.getName());
        }
        roleRepository.addRole("ROLE_" + req.getName().toUpperCase().replace(" ", "_"), priority);
    }

    @Override
    public void updateRole(RoleReq req) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkRolePriority(priority, req.getId());

        if(!roleRepository.existsById(req.getId())){
            throw new DodException(MessageCode.ROLE_NOT_FOUND);
        }

        if (roleRepository.existsByNameAndIdNot(req.getName(), req.getId())) {
            throw new DodException(MessageCode.ROLE_NAME_EXIST, req.getName());
        }
        roleRepository.updateRole(req.getId(), "ROLE_" + req.getName().toUpperCase().replace(" ", "_"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkRolePriority(priority, id);


        if (!roleRepository.existsById(id)) {
            throw new DodException(MessageCode.ROLE_NOT_EXIST);
        }
        if (accountRoleRepository.existsByRoleId(id)) {
            throw new DodException(MessageCode.ROLE_ALREADY_IN_USE);
        }
        roleRepository.deleteById(id);
    }
}

