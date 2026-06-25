package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String recipientEmail;
    private String recipientRole;
    private String message;
    private String type; // e.g. "TASK", "ASSIGNMENT", "REPORT"
    private UUID targetId; // ID of task/report to redirect to
    private boolean isRead;
    private LocalDateTime createdAt;
}
