package com.example.demo_oracle_db.config.authen;

import com.example.demo_oracle_db.config.authen.dto.FunctionInfo;
import com.example.demo_oracle_db.config.authen.dto.GrantedAuthorityCustom;
import com.example.demo_oracle_db.config.authen.dto.UserPrincipal;
import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Permission;
import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.AccountRoleRepository;
import com.example.demo_oracle_db.repository.PermissionRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DodUserDetailService implements UserDetailsService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private AccountRoleRepository accountRoleRepository;

//    public static final ThreadLocal<Account> ACCOUNT = new ThreadLocal<>();

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Integer priority = accountRoleRepository.getAccountPriority(account.getId()).orElseThrow(() -> new UsernameNotFoundException("Priority not found!"));
        List<Role> roles = roleRepository.findByAccount(account.getId());
        if (roles.isEmpty()) throw new UsernameNotFoundException("Role not found");

        List<Permission> permissions = permissionRepository.findPermissionsByRole(roles.stream().map(Role::getId).toList());

        Set<GrantedAuthorityCustom> grantedAuthorities = new HashSet<>();
        // Thêm các chức năng
        List<FunctionInfo> functionInfos = FunctionInfo.mapToFunctionInfo(permissions);
        if (!functionInfos.isEmpty()) {
            functionInfos.forEach(functionInfo ->
                    grantedAuthorities.add(new GrantedAuthorityCustom(functionInfo))
            );
        }
        return new UserPrincipal(account.getUsername(), account.getFullName() ,account.getPassword(), priority,roles.stream().map(Role::getName).toList(), grantedAuthorities);
    }
}
