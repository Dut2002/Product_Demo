package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.userPermisson.UserPermissionService;
import com.example.demo_oracle_db.service.userPermisson.request.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user-permission")
public class UserPermissionController {

    @Autowired
    UserPermissionService userPermissionService;

    @GetMapping("view-user-permissions")
    public ResponseEntity<?> getUserPermissions(@RequestParam Long id) {
        return ResponseEntity.ok(userPermissionService.getPermissionsByRole(id));
    }

    @PostMapping("add-user-function")
    public ResponseEntity<?> addUserFunction(@RequestBody AddUserFunction request) throws DodException {
        userPermissionService.addUserFunction(request);
        return ResponseEntity.ok(new Res().resOk("Add function for role successfully!"));
    }

    @PostMapping("add-user-new-function")
    public ResponseEntity<?> addUserNewFunction(@RequestBody AddUserNewFunction request) throws DodException {
        userPermissionService.addUserNewFunction(request);
        return ResponseEntity.ok(new Res().resOk("Add new function for role successfully!"));
    }

    @DeleteMapping("delete-user-function")
    public ResponseEntity<?> deleteUserFunction(@RequestBody DeleteUserFunctionRequest request) throws DodException {
        userPermissionService.deleteUserFunction(request);
        return ResponseEntity.ok(new Res().resOk("Delete function of role successfully!"));
    }


    @PostMapping("add-user-permission")
    public ResponseEntity<?> addPermission(@RequestBody @Valid AddUserPermissionRequest request) throws DodException {
        userPermissionService.addPermission(request);
        return ResponseEntity.ok(new Res().resOk("Add new permission successfully!"));
    }

    @PostMapping("add-new-user-permission")
    public ResponseEntity<?> addNewPermission(@RequestBody @Valid AddNewUserPermissionRequest request) throws DodException {
        userPermissionService.addNewPermission(request);
        return ResponseEntity.ok(new Res().resOk("Add new permission successfully!"));
    }

    @DeleteMapping("delete-user-permission")
    public ResponseEntity<?> deleteUserPermission(@RequestBody DeleteUserPermissionRequest request) throws DodException {
        userPermissionService.deleteUserPermission(request);
        return ResponseEntity.ok(new Res().resOk("Delete permission successfully!"));
    }
}
