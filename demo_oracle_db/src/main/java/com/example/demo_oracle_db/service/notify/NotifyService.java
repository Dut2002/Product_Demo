package com.example.demo_oracle_db.service.notify;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.notify.req.NotifyReadReq;
import com.example.demo_oracle_db.service.notify.req.NotifyRequest;
import com.example.demo_oracle_db.service.notify.res.NotifyDto;
import org.springframework.data.domain.Page;


public interface NotifyService {
    Page<NotifyDto> getNotify(NotifyRequest req) throws DodException;

    boolean hasUnreadNotify() throws DodException;

    void checkRead(NotifyReadReq req) throws DodException;

    void checkReadAll() throws DodException;
}
