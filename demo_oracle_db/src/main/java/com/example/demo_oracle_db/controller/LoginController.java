package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.LoginService;
import com.example.demo_oracle_db.service.login.request.LoginReq;
import com.example.demo_oracle_db.service.login.request.RegisReq;
import com.example.demo_oracle_db.service.login.response.Res;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("login")
    private ResponseEntity<?> login(@RequestBody @Valid LoginReq req) throws DodException {

        return ResponseEntity.ok(loginService.login(req));
    }

    @PostMapping("register")
    private ResponseEntity<?> register(@RequestBody @Valid RegisReq req) throws DodException {
        loginService.register(req);
        return ResponseEntity.ok(new Res().resOk("Register new User successfully"));
    }

    @PostMapping("logout")
    private ResponseEntity<?> logout(@RequestHeader Map<String, String> headers) {
        loginService.logout(headers);
        return ResponseEntity.ok(new Res().resOk("Log out successfully!"));

    }

    @PostMapping("check-refresh-token")
    private ResponseEntity<?> checkRefreshToken(@RequestBody String refreshToken) throws DodException {
        return ResponseEntity.ok(loginService.checkRefreshToken(refreshToken));
    }


}
