package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.mySupplier.SupplierService;
import com.example.demo_oracle_db.service.mySupplier.request.ProcessRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/supplier-approval")
public class SupplierManagerController {

    @Autowired
    SupplierService supplierService;

    @GetMapping("view-request")
    ResponseEntity<?> viewRequest(){
        return ResponseEntity.ok(supplierService.viewRequest());
    }

    @PostMapping ("process-request")
    ResponseEntity<?> processRequest(@RequestBody @Valid ProcessRequest request) throws DodException {
        supplierService.processRequest(request);
        return ResponseEntity.ok(new Res().resOk("The request was processed successfully!"));
    }
}
