package com.example.demo_oracle_db.service.role.impl;

import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.service.role.RoleService;
import com.example.demo_oracle_db.service.role.request.RoleReq;
import com.example.demo_oracle_db.util.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return (List<Role>) roleRepository.findAll();
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.findOne((root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("name"), name)
        ).orElse(null);
    }

    @Override
    public void addRole(RoleReq req) throws DodException {
        if(roleRepository.existsByName(req.getName())){
            throw new DodException(MessageCode.ROLE_NAME_EXIST, req.getName());
        }
        Role role = new Role();
        role.setName(req.getName());
        roleRepository.save(role);
    }

    @Override
    public void updateRole(RoleReq req) throws DodException {
        Role role = this.getById(req.getId());
        if(role == null){
            throw new DodException(MessageCode.ROLE_NOT_EXIST, req.getId());
        }
        Role checkName = this.getByName(role.getName());

        if(checkName!=null && !checkName.getId().equals(role.getId())){
            throw new DodException(MessageCode.ROLE_NAME_EXIST, role.getName());
        }
        role.setName(req.getName());
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) throws DodException {
        if(!roleRepository.existsById(id)){
            throw new DodException(MessageCode.ROLE_NOT_EXIST, id);
        }
        roleRepository.deleteById(id);
    }
}
