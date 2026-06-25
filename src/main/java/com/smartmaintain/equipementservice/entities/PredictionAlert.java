package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PredictionAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long engineId;
    private String engineName;
    private String message;
    private String type;
    private LocalDateTime timestamp;
    private String status;
}
