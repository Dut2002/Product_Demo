package com.example.demo_oracle_db.config.authen;

import com.example.demo_oracle_db.config.JwtConfig;
import com.example.demo_oracle_db.entity.Account;
import com.example.demo_oracle_db.entity.Function;
import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.repository.AccountRepository;
import com.example.demo_oracle_db.repository.FunctionRepository;
import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.util.MessageCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Date;
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
    FunctionRepository functionRepository;
    @Autowired
    RoleRepository roleRepository;



    //Xử lý filter để phân quyền
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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
            Date expireDate = claims.getExpiration();
            if (username != null) {
                List<String> authorities = (List<String>) claims.get("authorities");
                String roleName = (String) claims.get("role");
                Role role = roleRepository.findByName(roleName).orElseThrow(()-> new DodException(MessageCode.ROLE_NOT_FOUND));
                String endPoint = request.getRequestURI();
                Function function = functionRepository.findByEndPointAndRole(endPoint, role.getId()).orElseThrow(()-> new DodException(MessageCode.FUNCTION_NOT_FOUND));

                if(!authorities.contains(function.getName())){
                    throw new DodException(MessageCode.USER_UNAUTHORIZED);
                }

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
                Account account = accountRepository.findByUsername(username).orElse(null);
                if(account == null){
                    throw new DodException(MessageCode.USER_NAME_NOT_EXIST, username);
                }
                if(account.getAccessToken() == null){
                    throw new DodException(MessageCode.TOKEN_NOT_EXISTS,username);
                }else{
                    if(account.getAccessToken().equals(token) && expireDate.before(new Date())){
                        throw new DodException(MessageCode.TOKEN_EXPIRED);
                    }
                }
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e){
            SecurityContextHolder.clearContext();
        }

        System.out.println(response.getHeaderNames());
        System.out.println(request.getHeaderNames());

        // go to the next filter in the filter chain
        filterChain.doFilter(request, response);
    }
}
