package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.exception.DodException;
import com.example.demo_oracle_db.service.login.response.Res;
import com.example.demo_oracle_db.service.notify.NotifyService;
import com.example.demo_oracle_db.service.notify.req.NotifyReadReq;
import com.example.demo_oracle_db.service.notify.req.NotifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/notify")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    @PostMapping("get-notify")
    public ResponseEntity<?> getNotify(@RequestBody NotifyRequest req) throws DodException {
        return ResponseEntity.ok(notifyService.getNotify(req));
    }

    @GetMapping("has-unread-notify")
    public ResponseEntity<?> hasUnreadNotify() throws DodException {
        return ResponseEntity.ok(notifyService.hasUnreadNotify());
    }

    @PutMapping("check-read")
    public ResponseEntity<?> checkRead(@RequestBody NotifyReadReq req) throws DodException {
        notifyService.checkRead(req);
        return ResponseEntity.ok(new Res().resOk("Check Read Success"));
    }

    @PutMapping("check-read-all")
    public ResponseEntity<?> checkReadAll() throws DodException {
        notifyService.checkReadAll();
        return ResponseEntity.ok(new Res().resOk("Check Read All Success"));
    }
}
