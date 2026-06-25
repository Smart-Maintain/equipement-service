package com.smartmaintain.equipementservice.dto;

import java.util.List;
import java.util.UUID;

public record TacheRequest(
        String description,
        String priorite,
        String status,
        UUID equipeId,
        Long taxonomieId,
        UUID maintenanceId,
        List<SubTaskRequest> subTasks
) {
}
