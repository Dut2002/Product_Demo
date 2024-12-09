package com.example.demo_oracle_db.service.notify;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.notify.req.AddNotifyReq;

import java.util.List;

public interface AddNotifyService {
    void addNotify(AddNotifyReq notifyReq, List<Long> accountIds) throws DodException;

    void addSupplierRequest();
}
