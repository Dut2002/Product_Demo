package com.example.demo_oracle_db.config.authen;

import com.example.demo_oracle_db.config.JwtConfig;
import com.example.demo_oracle_db.config.authen.dto.FunctionInfo;
import com.example.demo_oracle_db.config.authen.dto.GrantedAuthorityCustom;
import com.example.demo_oracle_db.config.authen.dto.PermissionInfo;
import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.PermissionRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.util.MessageCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class JwtRequestFilter extends OncePerRequestFilter {

    //Config cho jwt
    @Autowired
    @Qualifier("jwtConfigAuth")
    private JwtConfig jwtConfig;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RoleRepository roleRepository;


    //Xử lý filter để phân quyền
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        //Lấy header chứa token
        String header = request.getHeader(jwtConfig.getHeader());
        //Check header có tồn tại hay bắt đầu với tiền tố thiết lập không?
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        //Loại bỏ tiền tố lấy token
        String token = header.replace(jwtConfig.getPrefix(), "").trim();
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            if (username != null) {
                Account account = accountRepository.findByUsername(username)
                        .orElseThrow(() -> new DodException(MessageCode.USER_NAME_NOT_EXIST, username));
                if (account.getAccessToken() == null) {
                    throw new DodException(MessageCode.TOKEN_NOT_EXISTS, username);
                }
                List<FunctionInfo> functionInfos = extractFucntionInfoFromClaims(claims);
                List<String> endPoints = new ArrayList<>();
                for (FunctionInfo functionInfo : functionInfos) {
                    for (PermissionInfo permissionInfo : functionInfo.getPermissions()) {
                        endPoints.add(permissionInfo.getBeEndPoint());
                    }
                }
                List<Role> roles = roleRepository.findByAccount(account.getId());
                List<Long> roleIds = roles.stream().map(Role::getId).toList();

                String endPoint = request.getRequestURI();
                if (this.permissionRepository.findByPermissionAccess(endPoint, roleIds).isEmpty()
                        || !endPoints.contains(endPoint)
                ) {
                    throw new DodException(MessageCode.USER_UNAUTHORIZED);
                }
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        roles.stream().map(Role::getName),
                        functionInfos.stream().map(GrantedAuthorityCustom::new).collect(Collectors.toList())
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        // go to the next filter in the filter chain
        filterChain.doFilter(request, response);
    }

    public List<FunctionInfo> extractFucntionInfoFromClaims(Claims claims) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<LinkedHashMap> functionInfoMaps = (List<LinkedHashMap>) claims.get("functions");

        List<FunctionInfo> functionInfos = new ArrayList<>();
        for (LinkedHashMap map : functionInfoMaps) {
            // Chuyển đổi từ LinkedHashMap sang FunctionInfo
            FunctionInfo functionInfo = objectMapper.convertValue(map, FunctionInfo.class);
            functionInfos.add(functionInfo);
        }

        return functionInfos;
    }
}
