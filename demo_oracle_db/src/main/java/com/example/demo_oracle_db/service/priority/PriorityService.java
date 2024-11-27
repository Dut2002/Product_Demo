package com.example.demo_oracle_db.service.priority;

import com.example.demo_oracle_db.config.authen.dto.UserPrincipal;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRoleRepository;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.util.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PriorityService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AccountRoleRepository accountRoleRepository;
    @Autowired
    FunctionRepository functionRepository;

    public Integer getCurrentUserPriority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UserPrincipal) {
            return ((UserPrincipal) auth).getPriority();
        }
        return null;
    }


    public void checkRolePriority(Integer priority, Long id) throws DodException {
        Integer rolePriority = roleRepository.findPriorityById(id).orElseThrow(() -> new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND));
        if (priority >= rolePriority) {
            throw new DodException(MessageCode.ACCOUNT_NOT_PERMISSION);
        }
    }

    public void checkAccountPriority(Integer priority, Long id) throws DodException {
        Integer accountPriority = accountRoleRepository.getAccountPriority(id).orElseThrow(() -> new DodException(MessageCode.ACCOUNT_PRIORITY_NOT_FOUND));
        if (priority >= accountPriority) {
            throw new DodException(MessageCode.ACCOUNT_NOT_PERMISSION);
        }
    }

    public void checkFunctionPriority(Integer priority, Long functionId) throws DodException {
        Integer functionPriority = functionRepository.findPriorityById(functionId).orElseThrow(() -> new DodException(MessageCode.FUNCTION_PRIORITY_NOT_FOUND));
        if (priority > functionPriority) {
            throw new DodException(MessageCode.ACCOUNT_NOT_PERMISSION);
        }
    }
}
