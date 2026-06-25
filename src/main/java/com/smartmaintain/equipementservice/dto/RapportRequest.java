package com.smartmaintain.equipementservice.dto;

import java.util.UUID;

public record RapportRequest(
        String type,
        UUID tacheId,
        UUID maintenanceId,
        String title,
        String content,
        String authorRole,
        String authorEmail,
        java.util.List<String> attachments
) {
}
