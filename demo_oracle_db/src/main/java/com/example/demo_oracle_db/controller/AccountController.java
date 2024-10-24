package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.user.AccountService;
import com.example.demo_oracle_db.service.user.request.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("view-users")
    private ResponseEntity<?> viewUsers(@RequestBody UserFilter request) {
        return ResponseEntity.ok(accountService.viewUsers(request));
    }

    @PostMapping("add-user")
    private ResponseEntity<?> addUser(@RequestBody @Valid AddUserReq req) throws DodException {
        accountService.addUser(req);
        return ResponseEntity.ok(new Res().resOk("Add new account successfully!"));
    }

    @PutMapping("edit-user")
    private ResponseEntity<?> editUser(@RequestBody @Valid EditUserReq req) throws DodException {
        accountService.updateUser(req);
        return ResponseEntity.ok(new Res().resOk("Update account successfully!"));
    }

    @PutMapping("change-status")
    private ResponseEntity<?> changeStatus(@RequestBody @Valid ChangeStatusReq req) throws DodException {
        accountService.changeStatus(req);
        return ResponseEntity.ok(new Res().resOk("Change status successfully!"));
    }

    @PutMapping("change-password")
    private ResponseEntity<?> changePassword(@RequestBody @Valid ChangePassReq req) throws DodException {
        accountService.changePassword(req);
        return ResponseEntity.ok(new Res().resOk("Change password successfully!"));
    }

    @DeleteMapping("delete-user")
    private ResponseEntity<?> deleteUser(@RequestParam Long id) throws DodException {
        accountService.deleteUser(id);
        return ResponseEntity.ok(new Res().resOk("Delete account successfully!"));
    }

    @PostMapping("add-role")
    private ResponseEntity<?> addRole(@RequestBody @Valid ChangeRoleUserReq req) throws DodException {
        accountService.addRole(req);
        return ResponseEntity.ok(new Res().resOk("Add role for account successfully!"));
    }

    @DeleteMapping("delete-role")
    private ResponseEntity<?> deleteRole(@RequestParam ChangeRoleUserReq req) throws DodException {
        accountService.deleteRole(req);
        return ResponseEntity.ok(new Res().resOk("Delete role of account successfully!"));
    }
}
