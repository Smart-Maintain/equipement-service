package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "maintenance_pieces")
@Data @NoArgsConstructor @AllArgsConstructor
public class MaintenancePiece {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "maintenance_id")
    private UUID maintenanceId;

    @Column(name = "piece_id")
    private UUID pieceId;

    @Column(name = "quantite_utilisee")
    private int quantiteUtilisee;
}