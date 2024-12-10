package com.example.demo_oracle_db.service.user.impl;

import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.AccountRole;
import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.AccountRoleRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.service.email.EmailService;
import com.example.demo_oracle_db.service.priority.PriorityService;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import com.example.demo_oracle_db.service.user.AccountService;
import com.example.demo_oracle_db.service.user.request.*;
import com.example.demo_oracle_db.service.user.response.UserRes;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import com.example.demo_oracle_db.util.PasswordUtil;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    @Autowired
    EntityManager entityManager;

    @Autowired
    EmailService emailService;

    @Autowired
    PriorityService priorityService;



    @Override
    public Page<UserRes> viewUsers(UserFilter request) throws DodException {
        if (request.getPageNum() == null) request.setPageNum(1);
        if (request.getPageSize() == null) request.setPageSize(8);
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);

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
            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            if (request.getRoleId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("accountRoles", JoinType.INNER).get("roleId"), request.getRoleId()));
            }

            assert query != null;
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<AccountRole> subRoot = subquery.from(AccountRole.class);



            predicates.add(criteriaBuilder.not(criteriaBuilder.exists(
                    subquery.select(criteriaBuilder.literal(1))
                            .where(criteriaBuilder.and(
                                    criteriaBuilder.equal(subRoot.get("accountId"), root.get("id")),
                                    criteriaBuilder.lessThanOrEqualTo(
                                            subRoot.join("role").get("priority"), priority
                                    )
                            ))
                    )
            ));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(request.getPageNum() - 1, request.getPageSize()));
        return UserRes.MapPageUser(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(AddUserReq req) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);

        if (accountRepository.existsByUsername(req.getUsername())) {
            throw new DodException(MessageCode.USER_NAME_ALREADY_EXISTS);
        }
        if (accountRepository.existsByEmail(req.getEmail())) {
            throw new DodException(MessageCode.EMAIL_ALREADY_EXISTS);
        }
        String password = PasswordUtil.generateRandomPassword(8);
        accountRepository.addAccount(req.getUsername(), bCryptPasswordEncoder.encode(password), req.getEmail(), req.getFullName(), Constants.Status.Pending.name());
        Long accountId = accountRepository.getLastInsertedId();
        List<Long> roleIds = new ArrayList<>(Arrays.stream(req.getRoles()).toList());
        Long roleUserId = roleRepository.findIdByName(Constants.Role.USER).orElseThrow(()->new DodException(MessageCode.ROLE_NOT_FOUND));
        if(!roleIds.contains(roleUserId))  roleIds.add(roleUserId);
        for (Long roleId : roleIds
        ) {
            priorityService.checkRolePriority(priority, roleId);
            if (!roleRepository.existsById(roleId)) {
                throw new DodException(MessageCode.ROLE_NOT_FOUND);
            }
            accountRoleRepository.addAccountRole(accountId, roleId);
        }

        CompletableFuture.supplyAsync(() -> {
            try {
                emailService.sendSimpleMail(
                        req.getEmail(),
                        "Thông tin về tài khoản của bạn trên hệ thống",
                        req.getFullName(),
                        "Tài khoản của bạnd đã được đăng ký",
                        "Đăng nhập hệ thống với mật khẩu <strong>" + password + "</strong>",
                        null, 0);
                System.out.println("Email đã được gửi thành công.");

            } catch (Exception e) {
                System.err.println("Send Mail Failed");
            }
            return null;
        });
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(EditUserReq req) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkAccountPriority(priority, req.getId());

        if (accountRepository.existsById(req.getId())) {
            throw new DodException(MessageCode.USER_NOT_FOUND);
        }
        if (accountRepository.existsByUsernameAndIdNot(req.getUsername(), req.getId())) {
            throw new DodException(MessageCode.USER_NAME_ALREADY_EXISTS, req.getUsername(),null);
        }
        if (accountRepository.existsByEmailAndIdNot(req.getEmail(), req.getId())) {
            throw new DodException(MessageCode.EMAIL_ALREADY_EXISTS, req.getUsername());
        }
        accountRepository.updateAccount(req.getId(), req.getUsername(), req.getEmail(), req.getFullName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(ChangeStatusReq req) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkAccountPriority(priority, req.getId());

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
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkAccountPriority(priority, req.getId());

        Account account = accountRepository.findById(req.getId())
                .orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));
        String password = PasswordUtil.generateRandomPassword(8);
        accountRepository.setPassword(account.getId(), bCryptPasswordEncoder.encode(password), Constants.Status.Pending.name());

        CompletableFuture.supplyAsync(() -> {
            try {
                emailService.sendSimpleMail(
                        account.getEmail(),
                        "Tài khoản của bạn đã được cấp mật khẩu mới",
                        account.getFullName(),
                        "Cập nhật mật khẩu mới",
                        "Mật khẩu mới của bạn là " + password,
                        null,
                        0
                );
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkAccountPriority(priority, id);


        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new DodException(MessageCode.USER_NOT_FOUND));
//        accountRoleRepository.deleteAll(account.getAccountRoles());
        if(account.getStatus() == Constants.Status.Blocked){
            throw new DodException(MessageCode.USER_ALREADY_BLOCKED);
        }
        accountRepository.setStatus(id, Constants.Status.Blocked.name());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(ChangeRoleUserReq req) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkAccountPriority(priority, req.getAccountId());

        if (!accountRepository.existsById(req.getAccountId())) throw new DodException(MessageCode.USER_NOT_FOUND);
        for (Long roleId : req.getRoleId()
        ) {
            Role role = roleRepository.findById(roleId).orElseThrow(()->new DodException(MessageCode.ROLE_NOT_FOUND));
            priorityService.checkRolePriority(priority, roleId);
            if(accountRoleRepository.existsByAccountIdAndRoleId(req.getAccountId(),roleId))
                throw new DodException(MessageCode.USER_ROLE_ALREADY_EXIST, role.getName());
            accountRoleRepository.addAccountRole(req.getAccountId(), roleId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(DeleteRoleUserReq req) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);
        priorityService.checkAccountPriority(priority, req.getAccountId());
        priorityService.checkRolePriority(priority, req.getRoleId());

        if (!accountRepository.existsById(req.getAccountId())) throw new DodException(MessageCode.USER_NOT_FOUND);
        if (!roleRepository.existsById(req.getRoleId())) throw new DodException(MessageCode.ROLE_NOT_FOUND);
        if (!accountRoleRepository.existsByAccountIdAndRoleId(req.getAccountId(), req.getRoleId()))
            throw new DodException(MessageCode.ACCOUNT_WITH_ROLE_NOT_FOUND);
        if (accountRoleRepository.existsByRoleIdAndRoleName(req.getRoleId(), Constants.Role.USER)) {
            throw new DodException(MessageCode.ROLE_USER_DEFAULT);
        }
        accountRoleRepository.deleteByAccountIdAndRoleId(req.getAccountId(), req.getRoleId());
    }

    @Override
    public List<SearchBox> getRoleSearch(Long id) throws DodException {
        Integer priority = priorityService.getCurrentUserPriority();
        if (priority == null) throw new DodException(MessageCode.ROLE_PRIORITY_NOT_FOUND);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SearchBox> query = criteriaBuilder.createQuery(SearchBox.class);
        Root<Role> root = query.from(Role.class);

        List<Predicate> predicates = new ArrayList<>();
        if (id != null) {
            // Subquery để kiểm tra Role đã được liên kết với Account
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<AccountRole> subRoot = subquery.from(AccountRole.class);

            // Điều kiện: accountRole.accountId = id AND accountRole.roleId = role.id
            subquery.select(subRoot.get("roleId"))
                    .where(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(subRoot.get("accountId"), id),
                                    criteriaBuilder.equal(subRoot.get("roleId"), root.get("id"))
                            )
                    );
            // Thêm NOT EXISTS vào điều kiện chính
            predicates.add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));
        }
        predicates.add(criteriaBuilder.notEqual(root.get("name"),Constants.Role.USER));
        predicates.add(criteriaBuilder.greaterThan(root.get("priority"), priority));

        query.multiselect(root.get("id"), root.get("name"));
        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
