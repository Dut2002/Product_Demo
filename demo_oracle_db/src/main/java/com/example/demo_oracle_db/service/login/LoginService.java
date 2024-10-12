package com.example.demo_oracle_db.service.login;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.request.LoginReq;
import com.example.demo_oracle_db.service.login.request.RegisReq;
import com.example.demo_oracle_db.service.login.response.LogRes;

import java.util.Map;

public interface LoginService
{
    LogRes login(LoginReq req) throws DodException;
    LogRes checkRefreshToken(String refreshToken) throws DodException;
    void register(RegisReq req) throws DodException;
    void logout(Map<String, String> headers);
    LogRes updateProfile();
    LogRes getProfile(Map<String, String> headers);
}
