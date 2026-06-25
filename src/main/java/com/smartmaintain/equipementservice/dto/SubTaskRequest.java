package com.smartmaintain.equipementservice.dto;

import java.util.UUID;

public record SubTaskRequest(
        String description,
        String status,
        UUID assignedMemberId,
        String assignedMemberName
) {
}
