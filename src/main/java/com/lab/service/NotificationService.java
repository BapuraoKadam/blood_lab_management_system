package com.lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab.entity.Notification;
import com.lab.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    public void createNotification(String message) {
        Notification n = new Notification();
        n.setMessage(message);
        repo.save(n);
    }

    public List<Notification> getUnread() {
        return repo.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public List<Notification> getAll() {
        return repo.findAll();
    }

    public void markAllRead() {
        List<Notification> list = repo.findAll();
        for (Notification n : list) {
            n.setRead(true);
        }
        repo.saveAll(list);
    }
}
