package com.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lab.entity.Notification;
import com.lab.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminNotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/notifications")
    public List<Notification> getNotifications() {
        return service.getUnread();
    }

    @PostMapping("/notifications/read")
    public String markRead() {
        service.markAllRead();
        return "OK";
    }
}