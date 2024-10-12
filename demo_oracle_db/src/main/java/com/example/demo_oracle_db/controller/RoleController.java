package com.example.demo_oracle_db.controller;


import com.example.demo_oracle_db.entity.Role;
import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.role.RoleService;
import com.example.demo_oracle_db.service.role.request.RoleReq;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("get-roles")
    public ResponseEntity<?> getRoles(){
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("get-role-by-id")
    public ResponseEntity<?> getRoleById(@RequestParam Long id){
        return ResponseEntity.ok(roleService.getById(id));
    }

    @GetMapping("get-role-by-name")
    public ResponseEntity<?> getRoleByName(@RequestParam String name){
        return ResponseEntity.ok(roleService.getByName(name));
    }

    @PostMapping("add-role")
    public ResponseEntity<?> addRole(@RequestBody @Valid RoleReq req) throws DodException {
        roleService.addRole(req);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Add new Role successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("update-role")
    public ResponseEntity<?> updateRole(@RequestBody @Valid RoleReq req) throws DodException {
        roleService.updateRole(req);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Update Role successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete-role")
    public ResponseEntity<?> deleteRole(@RequestParam Long id) throws DodException {
        roleService.deleteRole(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Delete Role successfully");
        return ResponseEntity.ok(response);
    }
}
