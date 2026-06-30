package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    // 'VALIDATED', 'REJECTED', 'IN_PROGRESS', 'COMPLETED'
    private String status;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "equipement_id")
    private Equipement equipement;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    // PREVENTIVE, CORRECTIVE, PREDICTIVE
    private String typeMaintenance;
}