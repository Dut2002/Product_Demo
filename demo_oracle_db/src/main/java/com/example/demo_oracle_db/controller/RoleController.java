package com.example.demo_oracle_db.controller;


import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.role.RoleService;
import com.example.demo_oracle_db.service.role.request.ChangePermissionRequest;
import com.example.demo_oracle_db.service.role.request.RoleReq;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("get-role-permission")
    public ResponseEntity<?> getRolePermission(@RequestParam Long id) {
        return ResponseEntity.ok(roleService.getRolePermission(id));
    }

    @GetMapping("get-roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @PostMapping("add-role")
    public ResponseEntity<?> addRole(@RequestBody @Valid RoleReq req) throws DodException {
        roleService.addRole(req);
        return ResponseEntity.ok(new Res().resOk("Add new Role successfully"));
    }

    @PutMapping("update-role")
    public ResponseEntity<?> updateRole(@RequestBody @Valid RoleReq req) throws DodException {
        roleService.updateRole(req);
        return ResponseEntity.ok(new Res().resOk("Update Role successfully"));
    }

    @DeleteMapping("delete-role")
    public ResponseEntity<?> deleteRole(@RequestParam Long id) throws DodException {
        roleService.deleteRole(id);
        return ResponseEntity.ok(new Res().resOk("Delete Role successfully"));
    }

    @PutMapping("change-permission")
    public ResponseEntity<?> changePermission(@RequestBody @Valid ChangePermissionRequest request) throws DodException {
        roleService.changePermission(request);
        return ResponseEntity.ok(new Res().resOk("Change Permission successfully"));

    }
}
