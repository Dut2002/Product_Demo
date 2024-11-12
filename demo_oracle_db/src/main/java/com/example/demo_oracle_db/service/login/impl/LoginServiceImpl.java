package com.example.demo_oracle_db.service.login.impl;

import com.example.demo_oracle_db.config.authen.TokenProvider;
import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.AccountRoleRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.service.login.LoginService;
import com.example.demo_oracle_db.service.login.request.LoginReq;
import com.example.demo_oracle_db.service.login.request.RegisReq;
import com.example.demo_oracle_db.service.login.response.LogRes;
import com.example.demo_oracle_db.util.Constants;
import com.example.demo_oracle_db.util.MessageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AccountRoleRepository accountRoleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TokenProvider tokenProvider;


    @Override
    public LogRes login(LoginReq req) throws DodException {
        Account account = accountRepository.findByUsername(req.getUsername()).orElseThrow(() -> new DodException(MessageCode.USER_NAME_NOT_EXIST, req.getUsername()));
        if (!bCryptPasswordEncoder.matches(req.getPassword(), account.getPassword())) {
            throw new DodException(MessageCode.PASSWORD_INCORRECT);
        }
        SecurityContextHolder.clearContext();
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account.getUsername(), req.getPassword()));
        String accessToken = tokenProvider.createToken(auth);
        String refreshToken = tokenProvider.createRefreshToken(auth);
        if (StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken)) {
            LogRes res = new LogRes();
            res.setUsername(account.getUsername());
            res.setEmail(account.getEmail());
            res.setFullName(account.getFullName());
            List<Role> roles = roleRepository.findByAccount(account.getId());
            if (roles.isEmpty()) throw new DodException(MessageCode.ROLE_NOT_FOUND);
            res.setRoles(roles.stream().map(Role::getName).toList());
            res.setStatus(account.getStatus());
            res.setToken(accessToken);
            res.setRefreshToken(refreshToken);
            try {
                accountRepository.updateToken(accessToken, refreshToken, account.getId());
                SecurityContextHolder.getContext().setAuthentication(auth);
                return res;
            } catch (Exception e) {
                throw new DodException(MessageCode.LOGIN_FAILED);
            }
        } else {
            throw new DodException(MessageCode.TOKEN_FAILED);
        }
    }

    @Override
    public LogRes checkRefreshToken(String refreshToken) throws DodException {
        if (tokenProvider.validateJwtToken(refreshToken)) {
            String username = tokenProvider.getUsername(refreshToken);
            Account account = accountRepository.findByUsername(username).orElseThrow(() -> new DodException(MessageCode.USER_NAME_NOT_EXIST, username));
            Authentication auth = tokenProvider.getAuthentication(refreshToken);
            if (refreshToken.equals(account.getRefreshToken())) {
                String accessToken = tokenProvider.createToken(auth);
                try {
                    // Tạo LogRes và thiết lập thông tin
                    LogRes res = new LogRes();
                    res.setUsername(account.getUsername());
                    res.setEmail(account.getEmail());
                    res.setFullName(account.getFullName());
                    List<Role> role = roleRepository.findByAccount(account.getId());
                    if (role.isEmpty()) throw new DodException(MessageCode.ROLE_NOT_FOUND);
                    res.setRoles(role.stream().map(Role::getName).toList());
                    res.setStatus(account.getStatus());
                    res.setToken(accessToken);
                    res.setRefreshToken(refreshToken);

                    // Lưu thông tin tài khoản với access token mới
                    accountRepository.updateToken(accessToken, refreshToken, account.getId());
                    return res;
                } catch (Exception e) {
                    throw new DodException(MessageCode.REFRESH_FAILED);
                }
            }
        }
        throw new DodException(MessageCode.TOKEN_INVALID);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisReq req) throws DodException {
        if (accountRepository.existsByUsername(req.getUsername())) {
            throw new DodException(MessageCode.USER_NAME_ALREADY_EXISTS, req.getUsername());
        }
        // Kiểm tra xem email đã tồn tại hay chưa
        if (accountRepository.existsByEmail(req.getEmail())) {
            throw new DodException(MessageCode.EMAIL_ALREADY_EXISTS, req.getEmail());
        }

        try {
            accountRepository.addAccount(
                    req.getUsername(),
                    bCryptPasswordEncoder.encode(req.getPassword()),
                    req.getEmail(),
                    req.getFullName()
            );
            Long accountId = accountRepository.getLastInsertedId();
            Long roleId = roleRepository.findIdByName(Constants.Role.CUSTOMER).orElseThrow(() -> new DodException(MessageCode.ROLE_NOT_FOUND));
            accountRoleRepository.addAccountRole(accountId, roleId);
        } catch (Exception e) {
            throw new DodException(MessageCode.REGISTER_USER_FAILED);
        }
    }

    @Override
    public void logout(Map<String, String> headers) {
        String token = tokenProvider.getToken(headers);
        String username = tokenProvider.getUsername(token);
        try {
            SecurityContextHolder.clearContext();
            accountRepository.logout(username);
        } catch (Exception e) {
            log.error("Delete Token Failed!");
        }
        SecurityContextHolder.clearContext();
    }

    @Override
    public LogRes updateProfile() {
        return null;
    }

    @Override
    public LogRes getProfile(Map<String, String> headers) {
        return null;
    }
}
