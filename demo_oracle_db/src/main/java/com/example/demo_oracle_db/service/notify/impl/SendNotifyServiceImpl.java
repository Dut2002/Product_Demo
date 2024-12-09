package com.example.demo_oracle_db.service.notify.impl;

import com.example.demo_oracle_db.repository.RoleRepository;
import com.example.demo_oracle_db.service.notify.req.AddNotifyReq;
import com.example.demo_oracle_db.service.notify.res.SendNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotifyServiceImpl implements SendNotifyService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoleRepository roleRepository;

    @Override
    public void sendNotification(String username, String message){
        log.info("send notification to {}", username);
        messagingTemplate.convertAndSendToUser(
                username,
                "/notification",
                message);
    }

    @Override
    public void sendGroupNotification(String roleName, String message) {
        log.info("Sending notification to group {}", roleName);
        // Gửi thông báo tới nhóm
        messagingTemplate.convertAndSend(
                "/topic/role/" + roleName+"/notification/",
                message
        );
    }

    @Override
    public void sendNotification(String username, AddNotifyReq addNotifyReq) {
        log.info("send notification to {}", username);
        messagingTemplate.convertAndSendToUser(
                username,
                "/notification",
                addNotifyReq);
    }

    @Override
    public void sendGroupNotification(String roleName, AddNotifyReq addNotifyReq) {
        log.info("Sending notification to group {}", roleName);
        // Gửi thông báo tới nhóm
        messagingTemplate.convertAndSend(
                "/topic/role/" + roleName+"/notification/",
                addNotifyReq
        );
    }
}
