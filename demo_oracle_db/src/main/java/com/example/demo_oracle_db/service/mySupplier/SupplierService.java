package com.example.demo_oracle_db.service.mySupplier;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.mySupplier.request.ProcessRequest;
import com.example.demo_oracle_db.service.mySupplier.request.RequestFilter;
import com.example.demo_oracle_db.service.mySupplier.request.SaveNoteRequest;
import com.example.demo_oracle_db.service.mySupplier.request.SupplierFilter;
import com.example.demo_oracle_db.service.mySupplier.response.RequestDto;
import com.example.demo_oracle_db.service.mySupplier.response.SupplierInfo;
import org.springframework.data.domain.Page;

public interface SupplierService {
    Page<RequestDto> viewRequest(RequestFilter filter);

    void processRequest(ProcessRequest request) throws DodException;

    Page<SupplierInfo> viewSupplier(SupplierFilter filter);

    void saveNote(SaveNoteRequest request) throws DodException;
}
