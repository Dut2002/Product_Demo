package com.example.demo_oracle_db.controller;


import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.mySupplier.request.CancelRequest;
import com.example.demo_oracle_db.service.supplier.mySupplier.MySupplierService;
import com.example.demo_oracle_db.service.supplier.mySupplier.request.OpenSupplierRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/supplier-info")
public class MySupplierController {

    @Autowired
    MySupplierService mySupplierService;

    @GetMapping("my-supplier-request")
    public ResponseEntity<?> mySupplierRequest() throws DodException {
        return ResponseEntity.ok(mySupplierService.mySupplierRequest());
    }

    @PostMapping("supplier-register")
    public ResponseEntity<?> supplierRegister(@RequestBody @Valid OpenSupplierRequest request) throws DodException {
        mySupplierService.supplierRegister(request);
        return ResponseEntity.ok(new Res().resOk("Send Request Successfully"));
    }

    @PutMapping("cancel-request")
    public ResponseEntity<?> rejectRequest(@RequestBody @Valid CancelRequest request) throws DodException {
        mySupplierService.cancelRequest(request);
        return ResponseEntity.ok(new Res().resOk("Send Request Successfully"));
    }

    @GetMapping("view-supplier-info")
    public ResponseEntity<?> viewSupplerInfo() throws DodException {
        return ResponseEntity.ok(mySupplierService.viewSupplerInfo());
    }
}
