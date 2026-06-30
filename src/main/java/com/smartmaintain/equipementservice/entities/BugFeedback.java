package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BugFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String reporterEmail;
    private String reporterRole;
    
    @Column(length = 2000)
    private String description;
    
    private LocalDateTime createdAt;
}
