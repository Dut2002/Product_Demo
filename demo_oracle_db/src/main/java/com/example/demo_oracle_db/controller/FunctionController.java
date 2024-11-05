package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.function.FunctionService;
import com.example.demo_oracle_db.service.function.request.*;
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

    @GetMapping("view-functions")
    public ResponseEntity<?> viewFunctions() {
        return ResponseEntity.ok(functionService.viewAll());

    }

    @PostMapping("add-function")
    public ResponseEntity<?> addFunction(@RequestBody AddFunctionRequest request) throws DodException {
        functionService.addFunction(request);
        return ResponseEntity.ok(new Res().resOk("Add new function successfully"));
    }

    @PutMapping("update-function")
    public ResponseEntity<?> updateFunction(@RequestBody UpdateFunctionRequest request) throws DodException {
        functionService.updateFunction(request);
        return ResponseEntity.ok(new Res().resOk("Update function successfully"));
    }

    @DeleteMapping("delete-function")
    public ResponseEntity<?> deleteFunction(@RequestParam Long id) throws DodException {
        functionService.deleteFunction(id);
        return ResponseEntity.ok(new Res().resOk("Delete function successfully"));
    }

    @PostMapping("add-permission")
    public ResponseEntity<?> addPermission(@RequestBody @Valid AddPermissionRequest request) throws DodException {
        functionService.addPermission(request);
        return ResponseEntity.ok(new Res().resOk("Add new permission successfully"));
    }

    @DeleteMapping("delete-permission")
    public ResponseEntity<?> deletePermission(@RequestParam Long id) throws DodException {
        functionService.deletePermission(id);
        return ResponseEntity.ok(new Res().resOk("Delete function successfully"));
    }

    @PutMapping("update-permission")
    public ResponseEntity<?> updatePermission(@RequestBody UpdatePermissionRequest request) throws DodException {
        functionService.updatePermission(request);
        return ResponseEntity.ok(new Res().resOk("Update function successfully"));
    }

    @DeleteMapping("check-delete")
    public ResponseEntity<?> checkDelete(Long id) throws DodException {
        return ResponseEntity.ok(functionService.checkDeletePermission(id));
    }
}
