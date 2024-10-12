package com.example.demo_oracle_db.config;

import com.example.demo_oracle_db.config.authen.DodUserDetailService;
import com.example.demo_oracle_db.config.authen.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityAuthConfig {
    @Autowired
    private DodUserDetailService dodUserDetailService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(dodUserDetailService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "DELETE", "POST"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.addAllowedMethod("*");
        // Cấu hình CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF
                // Thêm bộ lọc JWT để xác thực
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                // Cấu hình xử lý ngoại lệ cho các yêu cầu không được xác thực
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint()))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Cấu hình quản lý phiên
                .authorizeHttpRequests(auth -> auth // Cấu hình phân quyền truy cập
                        .requestMatchers(HttpMethod.POST, "/api/login/**").permitAll() // Cho phép tất cả yêu cầu POST đến /api/login/**
                        .requestMatchers(HttpMethod.GET, "/api/login/**").permitAll() // Cho phép tất cả yêu cầu GET đến /api/login/**
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll() // Cho phép tất cả yêu cầu GET đến /swagger-ui/**
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/send-otp-reset-password").permitAll() // Cho phép yêu cầu GET đến /api/user/send-otp-reset-password
                        .requestMatchers(HttpMethod.POST, "/api/user/reset-password").permitAll() // Cho phép yêu cầu POST đến /api/user/reset-password
                        .anyRequest().authenticated()); // Tất cả yêu cầu còn lại phải được xác thực
        return http.build();
    }
}
