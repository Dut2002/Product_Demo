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
    public ResponseEntity<?> getUserPermissions(@RequestParam Long id) throws DodException {
        return ResponseEntity.ok(userPermissionService.getPermissionsByRole(id));
    }

    @PostMapping("add-user-function")
    public ResponseEntity<?> addUserFunction(@RequestBody AddUserFunction request) throws DodException {
        userPermissionService.addUserFunction(request);
        return ResponseEntity.ok(new Res().resOk("Add function for role successfully!"));
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

    @DeleteMapping("delete-user-permission")
    public ResponseEntity<?> deleteUserPermission(@RequestBody DeleteUserPermissionRequest request) throws DodException {
        userPermissionService.deleteUserPermission(request);
        return ResponseEntity.ok(new Res().resOk("Delete permission successfully!"));
    }

    @GetMapping("get-function-search")
    public ResponseEntity<?> getFunctionSearch(@RequestParam Long id) throws DodException {
        return ResponseEntity.ok(userPermissionService.getFunctionSearch(id));
    }

    @GetMapping("get-permission-search")
    public ResponseEntity<?> getPermissionSearch(@RequestParam Long roleId, @RequestParam Long functionId) throws DodException {
        return ResponseEntity.ok(userPermissionService.getPermissionSearch(roleId,functionId));
    }

    @GetMapping("get-user-function-permission")
    public ResponseEntity<?> getUserFunctionPermission(@RequestParam Long roleId, @RequestParam Long functionId) throws DodException {
        return ResponseEntity.ok(userPermissionService.getUserFunctionPermission(roleId,functionId));
    }
}
