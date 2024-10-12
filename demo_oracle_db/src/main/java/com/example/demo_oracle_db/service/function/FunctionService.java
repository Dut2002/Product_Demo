package com.example.demo_oracle_db.service.function;

import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.function.request.AddFunctionReq;
import com.example.demo_oracle_db.service.function.request.RoleAccess;
import com.example.demo_oracle_db.service.function.request.UpdateFunctionReq;
import com.example.demo_oracle_db.service.function.request.UpdateRoleAccess;

import java.util.List;

public interface FunctionService {
    List<Function> getAll();

    void addFunction(AddFunctionReq req) throws DodException;

    void updateFunction(UpdateFunctionReq req) throws DodException;

    void modifyFunctionAccess(UpdateRoleAccess req) throws DodException;

    void deleteFunction(Long id) throws DodException;

    List<RoleAccess> getDetails(Long id) throws DodException;
}
