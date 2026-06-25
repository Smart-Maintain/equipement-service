package com.smartmaintain.equipementservice.controllers;

import com.smartmaintain.equipementservice.entities.Notification;
import com.smartmaintain.equipementservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam String email, @RequestParam String role) {
        return ResponseEntity.ok(service.getNotificationsForUser(email, role));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }
}
