package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.function.FunctionService;
import com.example.demo_oracle_db.service.function.request.AddPermissionRequest;
import com.example.demo_oracle_db.service.function.request.FunctionRequest;
import com.example.demo_oracle_db.service.function.request.UpdatePermissionRequest;
import com.example.demo_oracle_db.service.login.response.Res;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/function")
public class FunctionController {

    @Autowired
    FunctionService functionService;

    @GetMapping("get-functions")
    public ResponseEntity<?> getFunctions() {
        return ResponseEntity.ok(functionService.getAll());
    }

    @GetMapping("get-all-permission-details")
    public ResponseEntity<?> getAllPermissionDetail(@RequestParam Long id) throws DodException {
        return ResponseEntity.ok(functionService.getAllPermissionDetail(id));
    }

    @GetMapping("get-permission-details")
    public ResponseEntity<?> getPermissionDetail(@RequestParam Long id) throws DodException {
        return ResponseEntity.ok(functionService.getPermissionDetail(id));
    }

    @PostMapping("add-function")
    public ResponseEntity<?> addFunction(@RequestBody FunctionRequest request) throws DodException {
        functionService.addFunction(request);
        return ResponseEntity.ok(new Res().resOk("Add new function successfully!"));
    }

    @PostMapping("add-permission")
    public ResponseEntity<?> addPermission(@RequestBody @Valid AddPermissionRequest request) throws DodException {
        functionService.addPermission(request);
        return ResponseEntity.ok(new Res().resOk("Add new permission successfully!"));
    }

    @PutMapping("update-function")
    public ResponseEntity<?> updateFunction(@RequestBody @Valid FunctionRequest request) throws DodException {
        functionService.updateFunction(request);
        return ResponseEntity.ok(new Res().resOk("Update function successfully!"));
    }

    @PutMapping("update-permission")
    public ResponseEntity<?> updatePermission(@RequestBody @Valid UpdatePermissionRequest request) throws DodException {
        functionService.updatePermission(request);
        return ResponseEntity.ok(new Res().resOk("Update permission successfully!"));
    }

    @DeleteMapping("delete-function")
    public ResponseEntity<?> deleteFunction(@RequestParam Long id) throws DodException {
        functionService.deleteFunction(id);
        return ResponseEntity.ok(new Res().resOk("Delete function successfully!"));
    }

    @DeleteMapping("delete-permission")
    public ResponseEntity<?> deletePermission(@RequestParam Long id) throws DodException {
        functionService.deletePermission(id);
        return ResponseEntity.ok(new Res().resOk("Delete permission successfully!"));
    }
}
