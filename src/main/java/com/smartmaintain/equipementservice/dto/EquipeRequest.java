package com.smartmaintain.equipementservice.dto;

import java.util.List;
import java.util.UUID;

public record EquipeRequest(
        String nom,
        String specialite,
        UUID leaderEngineerId,
        String leaderEngineerName,
        List<UUID> technicianIds,
        List<String> technicianNames
) {
}
