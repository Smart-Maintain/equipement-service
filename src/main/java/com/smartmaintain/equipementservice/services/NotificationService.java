package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.entities.Notification;
import com.smartmaintain.equipementservice.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public Notification sendNotification(String email, String role, String message, String type, UUID targetId) {
        Notification notif = Notification.builder()
                .recipientEmail(email)
                .recipientRole(role != null ? role.toUpperCase() : null)
                .message(message)
                .type(type)
                .targetId(targetId)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        return repository.save(notif);
    }

    public List<Notification> getNotificationsForUser(String email, String role) {
        return repository.findByRecipient(email, role != null ? role.toUpperCase() : "");
    }

    public Notification markAsRead(UUID id) {
        Notification notif = repository.findById(id).orElseThrow();
        notif.setRead(true);
        return repository.save(notif);
    }
}
