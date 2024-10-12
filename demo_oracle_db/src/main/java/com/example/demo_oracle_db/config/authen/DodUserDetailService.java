package com.example.demo_oracle_db.config.authen;

import com.example.demo_oracle_db.config.authen.dto.UserPrincipal;
import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DodUserDetailService implements UserDetailsService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FunctionRepository functionRepository;

//    public static final ThreadLocal<Account> ACCOUNT = new ThreadLocal<>();

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
//        ACCOUNT.set(account);
        Role role = roleRepository.findById(account.getRoleId())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found"));

        List<Function> funcList = functionRepository.findByRole(role.getId());

        List<String> functions = funcList.stream()
                .map(Function::getName)
                .toList();

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // Thêm các chức năng
        functions.forEach(functionName ->
                grantedAuthorities.add(new SimpleGrantedAuthority(functionName))
        );
        return new UserPrincipal(account.getUsername(), account.getPassword(), role.getName(),grantedAuthorities);
    }
}
