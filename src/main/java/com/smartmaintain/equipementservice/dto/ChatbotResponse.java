package com.smartmaintain.equipementservice.dto;

import java.time.LocalDateTime;

public record ChatbotResponse(String answer, LocalDateTime generatedAt) {
}
