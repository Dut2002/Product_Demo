package com.example.demo_oracle_db.controller;

import com.example.demo_oracle_db.service.notify.res.SendNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/web-socket")
public class WebSocketTestController {

    private final SendNotifyService sendNotifyService;

    @GetMapping("send-user")
    public ResponseEntity<?> sendUser(@RequestParam String username) {
        sendNotifyService.sendNotification(username, "New user message for " + username);
        return ResponseEntity.ok("send success");
    }

    @GetMapping("send-group")
    public ResponseEntity<?> sendGroup(@RequestParam String groupName) {
        sendNotifyService.sendGroupNotification(groupName, "New group message for "+ groupName);
        return ResponseEntity.ok("send success");
    }
}
