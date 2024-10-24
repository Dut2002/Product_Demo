package com.example.demo_oracle_db.config.authen;

import com.example.demo_oracle_db.config.JwtConfig;
import com.example.demo_oracle_db.config.authen.dto.FunctionInfo;
import com.example.demo_oracle_db.config.authen.dto.GrantedAuthorityCustom;
import com.example.demo_oracle_db.config.authen.dto.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TokenProvider {


    @Autowired
    private DodUserDetailService dodUserDetailService;
    @Autowired
    @Qualifier("jwtConfigAuth")
    private JwtConfig jwtConfig;

    public String getToken(Map<String, String> headers) {
        return headers.get(jwtConfig.getHeader().toLowerCase()).replace(jwtConfig.getPrefix(), "");
    }

    public String createToken(Authentication authentication) {
        try {
            Date date = new Date();
            long now = date.getTime();
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            List<String> roles = userPrincipal.getRoles();
            // Lấy danh sách quyền (authorities) và endpoint
            List<GrantedAuthorityCustom> authorities = authentication.getAuthorities().stream()
                    .map(auth -> (GrantedAuthorityCustom) auth)
                    .toList();
            List<FunctionInfo> functionInfos = authorities.stream().map(GrantedAuthorityCustom::getFunctionInfo)
                    .toList();
            return Jwts.builder().setSubject(authentication.getName())
                    .claim("roles", roles)
                    .claim("functions", functionInfos)
                    .setIssuedAt(date)
                    .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000L))//milliseconds
                    .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                    .compact();
        } catch (Exception e) {
            log.error("Create Token Failed!");
            return "";
        }
    }

    public String createRefreshToken(Authentication authentication) {
        try {
            Date date = new Date();
            long now = date.getTime();
            return Jwts.builder().setSubject(authentication.getName())
                    .setIssuedAt(date)
                    .setExpiration(new Date(now + jwtConfig.getRefreshExpiration() * 1000L))//milliseconds
                    .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                    .compact();
        } catch (Exception e) {
            log.error("Create Refresh Token Failed!");
            return "";
        }
    }

    public Authentication getAuthentication(String token) {
        UserPrincipal userPrincipal = dodUserDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userPrincipal, userPrincipal.getRoles(), userPrincipal.getAuthorities());
    }

    public String getUsername(String token) {
        try {
            // Xử lý token bình thường nếu token hợp lệ
            return Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            // Nếu token hết hạn, vẫn lấy được thông tin từ phần claims
            Claims claims = e.getClaims();
            return claims.getSubject(); // Lấy username từ claims mà không cần xác thực
        } catch (SignatureException e) {
            // Token không hợp lệ
            throw new RuntimeException("Invalid token signature", e);
        } catch (Exception e) {
            // Xử lý các lỗi khác (nếu có)
            throw new RuntimeException("Error parsing token", e);
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtConfig.getSecret().getBytes())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
