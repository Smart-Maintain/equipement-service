package com.smartmaintain.equipementservice.dto;

public record PredictionAlertRequest(
        Long engineId,
        String engineName,
        String message,
        String type
) {
}
