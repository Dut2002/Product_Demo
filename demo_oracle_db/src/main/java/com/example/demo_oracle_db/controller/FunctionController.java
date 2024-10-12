package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.function.FunctionService;
import com.example.demo_oracle_db.service.function.request.AddFunctionReq;
import com.example.demo_oracle_db.service.function.request.UpdateFunctionReq;
import com.example.demo_oracle_db.service.function.request.UpdateRoleAccess;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/function")
public class FunctionController {

    @Autowired
    FunctionService functionService;

    @GetMapping("get-functions")
    public ResponseEntity<?> getFunctions()
    {
        return ResponseEntity.ok(functionService.getAll());
    }

    @GetMapping("get-function-detail")
    public ResponseEntity<?> getFunctionDetail(@RequestParam Long id) throws DodException {
        return ResponseEntity.ok(functionService.getDetails(id));
    }

    @PostMapping("add-function")
    public  ResponseEntity<?> addFunction(@RequestBody AddFunctionReq req) throws DodException {
        functionService.addFunction(req);
        return ResponseEntity.ok(new Res().resOk("Add new function successfully!"));
    }

    @PutMapping("update-function")
    public  ResponseEntity<?> updateFunction(@RequestBody UpdateFunctionReq req) throws DodException {
        functionService.updateFunction(req);
        return ResponseEntity.ok(new Res().resOk("Update function successfully!"));
    }

    @PutMapping("modify-function-access")
    public  ResponseEntity<?> modifyFunctionAccess(@RequestBody UpdateRoleAccess req) throws DodException {
        functionService.modifyFunctionAccess(req);
        return ResponseEntity.ok(new Res().resOk("Modify function access successfully!"));
    }

    @DeleteMapping("delete-function")
    public  ResponseEntity<?> deleteFunction(@RequestParam Long id) throws DodException {
        functionService.deleteFunction(id);
        return ResponseEntity.ok(new Res().resOk("Delete function successfully!"));
    }
}
