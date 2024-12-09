package com.example.demo_oracle_db.service.notify.res;

import com.example.demo_oracle_db.service.notify.req.AddNotifyReq;

public interface SendNotifyService {
//    void sendNotification(String username);
//
//    void sendGroupNotification(String roleName);

    void sendNotification(String username, String message);

    void sendGroupNotification(String roleName, String message);

    void sendNotification(String username, AddNotifyReq addNotifyReq);

    void sendGroupNotification(String roleName, AddNotifyReq addNotifyReq);
}
