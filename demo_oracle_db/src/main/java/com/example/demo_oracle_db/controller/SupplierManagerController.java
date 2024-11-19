package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.mySupplier.SupplierService;
import com.example.demo_oracle_db.service.mySupplier.request.ProcessRequest;
import com.example.demo_oracle_db.service.mySupplier.request.SupplierFilter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/supplier")
public class SupplierManagerController {

    @Autowired
    SupplierService supplierService;

    @PostMapping("view-supplier")
    ResponseEntity<?> viewSupplier(@RequestBody SupplierFilter filter){
        return ResponseEntity.ok(supplierService.viewSupplier(filter));
    }

}
