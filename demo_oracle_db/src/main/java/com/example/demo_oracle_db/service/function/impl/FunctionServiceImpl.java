package com.example.demo_oracle_db.service.function.impl;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.RoleFunction;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.RoleFunctionRepository;
import com.example.demo_oracle_db.service.function.FunctionService;
import com.example.demo_oracle_db.service.function.request.AddFunctionReq;
import com.example.demo_oracle_db.service.function.request.RoleAccess;
import com.example.demo_oracle_db.service.function.request.UpdateFunctionReq;
import com.example.demo_oracle_db.service.function.request.UpdateRoleAccess;
import com.example.demo_oracle_db.util.MessageCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionServiceImpl implements FunctionService {
    @Autowired
    FunctionRepository functionRepository;
    @Autowired
    RoleFunctionRepository roleFunctionRepository;

    @Override
    public List<Function> getAll() {
        return functionRepository.findAll((Specification<Function>) (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("endPoint")));
            return criteriaBuilder.and();
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFunction(AddFunctionReq req) throws DodException {
        if(functionRepository.existsByName(req.getName())){
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, req.getName());
        }
        if(functionRepository.existsByEndPoint(req.getEndPoint())){
            throw new DodException(MessageCode.FUNCTION_ENDPOINT_EXISTS, req.getEndPoint());
        }
        Function function = new Function();
        function.setEndPoint(req.getEndPoint());
        function.setName(req.getName());
        function = functionRepository.save(function);
        List<RoleFunction> roleFunctions = new ArrayList<>();
        for(RoleAccess r: req.getRoleAccesses()){
            RoleFunction roleFunction = new RoleFunction();
            roleFunction.setRoleId(r.getRoleId());
            roleFunction.setStatus(r.isPermission()?1:0);
            roleFunction.setFunctionId(function.getId());
            roleFunctions.add(roleFunction);
        }
        roleFunctionRepository.saveAll(roleFunctions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFunction(UpdateFunctionReq req) throws DodException {
        Function function = functionRepository.findById(req.getId()).orElseThrow(() -> new DodException(MessageCode.FUNCTION_NOT_FOUND));
        Function checkName = functionRepository.findByName(req.getName()).orElse(null);
        if(checkName!=null && !checkName.getId().equals(function.getId())){
            throw new DodException(MessageCode.FUNCTION_NAME_EXISTS, req.getName());
        }
        Function checkEndPoint = functionRepository.findByEndPoint(req.getEndPoint()).orElse(null);
        if(checkEndPoint!=null && !checkEndPoint.getId().equals(function.getId())){
            throw new DodException(MessageCode.FUNCTION_ENDPOINT_EXISTS, req.getEndPoint());
        }
        function.setName(req.getName());
        function.setEndPoint(req.getEndPoint());
        functionRepository.save(function);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyFunctionAccess(UpdateRoleAccess req) throws DodException {
        if(!functionRepository.existsById(req.getId())){
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        List<RoleFunction> roleFunctions = new ArrayList<>();
        for(RoleAccess r: req.getRoleAccesses()){
            RoleFunction roleFunction = roleFunctionRepository.findByRoleIdAndFunctionId(r.getRoleId(), req.getId()).orElse(new RoleFunction(r.getRoleId(), req.getId()));
            roleFunction.setStatus(r.isPermission()?1:0);
            roleFunctions.add(roleFunction);
        }
        roleFunctionRepository.saveAll(roleFunctions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunction(Long id) throws DodException {
        if(!functionRepository.existsById(id)){
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        List<RoleFunction> roleFunctions = roleFunctionRepository.findAllByFunctionId(id);
        roleFunctionRepository.deleteAll(roleFunctions);
        functionRepository.deleteById(id);
    }

    @Override
    public List<RoleAccess> getDetails(Long id) throws DodException {
        if(!functionRepository.existsById(id)){
            throw new DodException(MessageCode.FUNCTION_NOT_FOUND);
        }
        List<RoleAccess> roleAccesses = roleFunctionRepository.getFunctionDetail(id).stream()
                .map(RoleAccess::new).toList();
        return roleAccesses;
    }
}
