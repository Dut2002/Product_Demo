package com.example.demo_oracle_db.service.mySupplier;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.mySupplier.request.ProcessRequest;
import com.example.demo_oracle_db.service.mySupplier.response.RequestDto;

import java.util.List;

public interface SupplierService {
    List<RequestDto> viewRequest();

    void processRequest(ProcessRequest request) throws DodException;
}
