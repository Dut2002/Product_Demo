package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.fixSystem.FixSystemService;
import com.example.demo_oracle_db.service.fixSystem.request.AddSuperAdminFunction;
import com.example.demo_oracle_db.service.fixSystem.request.AddSuperAdminPermission;
import com.example.demo_oracle_db.service.function.request.AddFunctionRequest;
import com.example.demo_oracle_db.service.function.request.AddPermissionRequest;
import com.example.demo_oracle_db.service.login.response.Res;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/fix-system")
public class FixSystemController {

    @Autowired
    FixSystemService fixSystemService;

    @GetMapping("view-function")
    public ResponseEntity<?> viewFunction() throws DodException {
        return ResponseEntity.ok(fixSystemService.viewFunction());
    }

    @PostMapping("add-function")
    public ResponseEntity<?> addFunction(@RequestBody AddSuperAdminFunction request) throws DodException {
        fixSystemService.addFunction(request);
        return ResponseEntity.ok(new Res().resOk("Add function successfully!"));
    }


    @DeleteMapping("delete-function")
    public ResponseEntity<?> deleteFunction(@RequestParam Long functionId) throws DodException {
        fixSystemService.deleteFunction(functionId);
        return ResponseEntity.ok(new Res().resOk("Delete function successfully!"));
    }


    @PostMapping("add-permission")
    public ResponseEntity<?> addPermission(@RequestBody @Valid AddSuperAdminPermission request) throws DodException {
        fixSystemService.addPermission(request);
        return ResponseEntity.ok(new Res().resOk("Add new permission successfully!"));
    }

    @DeleteMapping("delete-permission")
    public ResponseEntity<?> deletePermission(@RequestParam Long permissionId) throws DodException {
        fixSystemService.deletePermission(permissionId);
        return ResponseEntity.ok(new Res().resOk("Delete permission successfully!"));
    }

    @GetMapping("get-function-search")
    public ResponseEntity<?> getFunctionSearch() throws DodException {
        return ResponseEntity.ok(fixSystemService.getFunctionSearch());
    }

    @GetMapping("get-permission-search")
    public ResponseEntity<?> getPermissionSearch(@RequestParam Long functionId) throws DodException {
        return ResponseEntity.ok(fixSystemService.getPermissionSearch(functionId));
    }

    @GetMapping("get-function-permission")
    public ResponseEntity<?> getUserFunctionPermission(@RequestParam Long functionId) throws DodException {
        return ResponseEntity.ok(fixSystemService.getUserFunctionPermission(functionId));
    }

    @PostMapping("add-new-function")
    public ResponseEntity<?> addNewFunction(@RequestBody AddFunctionRequest request) throws DodException {
        fixSystemService.addNewFunction(request);
        return ResponseEntity.ok(new Res().resOk("Add new function successfully"));
    }

    @PostMapping("add-new-permission")
    public ResponseEntity<?> addNewPermission(@RequestBody @Valid AddPermissionRequest request) throws DodException {
        fixSystemService.addNewPermission(request);
        return ResponseEntity.ok(new Res().resOk("Add new permission successfully"));
    }
}
