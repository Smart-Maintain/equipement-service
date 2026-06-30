package com.smartmaintain.equipementservice.dto;

public record PredictionLogRequest(Long engineId, String engineName, boolean alert, int rulCycles) {}
