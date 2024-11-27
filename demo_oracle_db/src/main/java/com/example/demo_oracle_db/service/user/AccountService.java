package com.example.demo_oracle_db.service.user;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.product.response.SearchBox;
import com.example.demo_oracle_db.service.user.request.*;
import com.example.demo_oracle_db.service.user.response.UserRes;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {
    Page<UserRes> viewUsers(UserFilter request) throws DodException;

    void addUser(AddUserReq req) throws DodException;

    void updateUser(EditUserReq req) throws DodException;

    void changeStatus(ChangeStatusReq req) throws DodException;

    void changePassword(ChangePassReq req) throws DodException;

    void deleteUser(Long id) throws DodException;

    void addRole(ChangeRoleUserReq req) throws DodException;

    void deleteRole(DeleteRoleUserReq req) throws DodException;

    List<SearchBox> getRoleSearch(Long id) throws DodException;
}
