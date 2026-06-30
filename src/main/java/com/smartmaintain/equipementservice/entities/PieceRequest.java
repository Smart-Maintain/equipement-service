package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PieceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "tache_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"pieceRequests", "subTasks", "maintenance", "equipe"})
    private Tache tache;

    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private Piece piece;

    private int quantite;

    private String status; // "PENDING", "APPROVED", "REJECTED"

    private String requestedBy; // Nom ou ID de l'utilisateur qui demande

    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
}
