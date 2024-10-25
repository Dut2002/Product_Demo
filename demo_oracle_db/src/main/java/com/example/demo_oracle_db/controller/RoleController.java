package com.example.demo_oracle_db.controller;


import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.role.RoleService;
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

    @GetMapping("get-roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @PostMapping("add-role")
    public ResponseEntity<?> addRole(@RequestBody @Valid RoleReq req) throws DodException {
        roleService.addRole(req);
        return ResponseEntity.ok(new Res().resOk("Add new Role successfully"));
    }

    @PutMapping("change-name")
    public ResponseEntity<?> updateRole(@RequestBody @Valid RoleReq req) throws DodException {
        roleService.updateRole(req);
        return ResponseEntity.ok(new Res().resOk("Change role name successfully"));
    }

    @DeleteMapping("delete-role")
    public ResponseEntity<?> deleteRole(@RequestParam Long id) throws DodException {
        roleService.deleteRole(id);
        return ResponseEntity.ok(new Res().resOk("Delete Role successfully"));
    }
}
