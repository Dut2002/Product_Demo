package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.mySupplier.SupplierService;
import com.example.demo_oracle_db.service.mySupplier.request.ProcessRequest;
import com.example.demo_oracle_db.service.mySupplier.request.RequestFilter;
import com.example.demo_oracle_db.service.mySupplier.request.SaveNoteRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/supplier-approval")
public class SupplierApprovalController {

    @Autowired
    SupplierService supplierService;

    @PostMapping("view-request")
    ResponseEntity<?> viewRequest(RequestFilter filter) {
        return ResponseEntity.ok(supplierService.viewRequest(filter));
    }

    @PutMapping("process-request")
    ResponseEntity<?> processRequest(@RequestBody @Valid ProcessRequest request) throws DodException {
        supplierService.processRequest(request);
        return ResponseEntity.ok(new Res().resOk("The request was processed successfully!"));
    }

    @PutMapping("save-note")
    ResponseEntity<?> saveNote(@RequestBody @Valid SaveNoteRequest request) throws DodException {
        supplierService.saveNote(request);
        return ResponseEntity.ok(new Res().resOk("The note was saved successfully!"));
    }
}
