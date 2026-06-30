package com.smartmaintain.equipementservice.controllers;

import jakarta.validation.Valid;

import com.smartmaintain.equipementservice.dto.PredictionAlertRequest;
import com.smartmaintain.equipementservice.entities.PredictionAlert;
import com.smartmaintain.equipementservice.entities.Tache;
import com.smartmaintain.equipementservice.services.PredictionAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/alerts")
public class PredictionAlertController {

    private final PredictionAlertService alertService;
    private final com.smartmaintain.equipementservice.services.PredictionAggregationService aggregationService;

    public PredictionAlertController(PredictionAlertService alertService, com.smartmaintain.equipementservice.services.PredictionAggregationService aggregationService) {
        this.alertService = alertService;
        this.aggregationService = aggregationService;
    }

    @PostMapping("/log")
    public ResponseEntity<Void> logPrediction(@Valid @RequestBody com.smartmaintain.equipementservice.dto.PredictionLogRequest request) {
        aggregationService.addPredictionLog(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<PredictionAlert> createAlert(@Valid @RequestBody PredictionAlertRequest request) {
        return ResponseEntity.ok(alertService.createAlert(request));
    }

    @GetMapping
    public ResponseEntity<List<PredictionAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @PostMapping("/{id}/convert")
    public ResponseEntity<Tache> convertToTask(@PathVariable Long id, @RequestParam(required = false) UUID equipeId) {
        return ResponseEntity.ok(alertService.convertToTask(id, equipeId));
    }
}
