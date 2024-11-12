package com.example.demo_oracle_db.service.user.impl;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.AccountRole;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.AccountRoleRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.service.user.AccountService;
import com.example.demo_oracle_db.service.user.request.*;
import com.example.demo_oracle_db.service.user.response.UserRes;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AccountRoleRepository accountRoleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<UserRes> viewUsers(UserFilter request) {
        if (request.getPageNum() == null) request.setPageNum(1);
        if (request.getPageSize() == null) request.setPageSize(8);

        Page<Account> users = accountRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getUsername() != null && !request.getUsername().isBlank()) {
                String searchKey = request.getUsername().trim().toLowerCase();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + searchKey + "%"));
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                String searchKey = request.getEmail().trim().toLowerCase();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + searchKey + "%"));
            }
            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                String searchKey = request.getFullName().trim().toLowerCase();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + searchKey + "%"));
            }
            if (request.getStatus() != null && !request.getStatus().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            if (request.getRoleId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("accountRoles", JoinType.INNER).get("roleId"), request.getRoleId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(request.getPageNum() - 1, request.getPageSize()));
        return UserRes.MapPageUser(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(AddUserReq req) throws DodException {
        if (accountRepository.existsByUsername(req.getUsername())) {
            throw new DodException(MessageCode.USER_NAME_ALREADY_EXISTS);
        }
        if (accountRepository.existsByEmail(req.getEmail())) {
            throw new DodException(MessageCode.EMAIL_ALREADY_EXISTS);
        }
         accountRepository.addAccount(
                req.getUsername(),
                bCryptPasswordEncoder.encode(req.getPassword()),
                req.getEmail(),
                req.getFullName());
        Long accountId = accountRepository.getLastInsertedId();
        for (Long roleId : req.getRoles()
        ) {
            if (!roleRepository.existsById(roleId)) {
                throw new DodException(MessageCode.ROLE_NOT_FOUND);
            }
            accountRoleRepository.addAccountRole(Long.valueOf(accountId), roleId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(EditUserReq req) throws DodException {
        if (accountRepository.existsById(req.getId())) {
            throw new DodException(MessageCode.USER_NOT_FOUND);
        }
        if (accountRepository.existsByUsernameAndIdNot(req.getUsername(), req.getId())) {
            throw new DodException(MessageCode.USER_NAME_ALREADY_EXISTS, req.getUsername());
        }
        if (accountRepository.existsByEmailAndIdNot(req.getEmail(), req.getId())) {
            throw new DodException(MessageCode.EMAIL_ALREADY_EXISTS, req.getUsername());
        }
        accountRepository.updateAccount(req.getId(), req.getUsername(), req.getEmail(), req.getFullName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(ChangeStatusReq req) throws DodException {
        Account account = accountRepository.findById(req.getId())
                .orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));
        Constants.Status status;
        try {
            status = Constants.Status.valueOf(req.getStatus());
        } catch (IllegalArgumentException e) {
            throw new DodException(MessageCode.STATUS_NOT_FOUND);
        }
        account.setStatus(status);
        accountRepository.changeStatus(req.getId(), status.name());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(ChangePassReq req) throws DodException {
        Account account = accountRepository.findById(req.getId())
                .orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));
        account.setPassword(bCryptPasswordEncoder.encode(req.getPassword().trim()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) throws DodException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));
        accountRoleRepository.deleteAll(account.getAccountRoles());
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(ChangeRoleUserReq req) throws DodException {
        if (accountRepository.existsById(req.getAccountId())) throw new DodException(MessageCode.USER_NOT_FOUND);
        if (roleRepository.existsById(req.getRoleId())) throw new DodException(MessageCode.ROLE_NOT_FOUND);
        AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(req.getAccountId());
        accountRole.setRoleId(req.getRoleId());
        accountRoleRepository.save(accountRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(ChangeRoleUserReq req) throws DodException {
        if (accountRepository.existsById(req.getAccountId())) throw new DodException(MessageCode.USER_NOT_FOUND);
        if (roleRepository.existsById(req.getRoleId())) throw new DodException(MessageCode.ROLE_NOT_FOUND);
        if (accountRoleRepository.existsByAccountIdAndRoleId(req.getAccountId(), req.getRoleId()))
            throw new DodException(MessageCode.ACCOUNT_WITH_ROLE_NOT_FOUND);
        accountRoleRepository.deleteByAccountIdAndRoleId(req.getAccountId(), req.getRoleId());
    }
}
