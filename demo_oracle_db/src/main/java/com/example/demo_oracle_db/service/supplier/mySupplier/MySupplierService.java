package com.example.demo_oracle_db.service.supplier.mySupplier;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.mySupplier.request.CancelRequest;
import com.example.demo_oracle_db.service.mySupplier.response.MyRequestDto;
import com.example.demo_oracle_db.service.mySupplier.response.SupplierInfo;
import com.example.demo_oracle_db.service.supplier.mySupplier.request.OpenSupplierRequest;

import java.util.List;

public interface MySupplierService {
    void supplierRegister(OpenSupplierRequest request) throws DodException;

    List<MyRequestDto> mySupplierRequest() throws DodException;

    SupplierInfo viewSupplerInfo() throws DodException;

    void cancelRequest(CancelRequest request) throws DodException;
}
