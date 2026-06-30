package com.smartmaintain.equipementservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public record MaintenanceRequest(
        String description,
        String status,
        LocalDate dateDebut,
        LocalDate dateFin,
        Long equipementId,
        UUID equipeId,
        String typeMaintenance
) {
}
