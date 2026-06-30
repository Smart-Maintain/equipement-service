package com.smartmaintain.equipementservice.controllers;

import jakarta.validation.Valid;

import com.smartmaintain.equipementservice.dto.MaintenanceRequest;
import com.smartmaintain.equipementservice.entities.Maintenance;
import com.smartmaintain.equipementservice.services.MaintenanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/maintenances")
public class MaintenanceController {
    private final MaintenanceService service;

    public MaintenanceController(MaintenanceService service) { this.service = service; }

    @GetMapping
    public List<Maintenance> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Maintenance getById(@PathVariable UUID id) { return service.getById(id); }

    @PostMapping
    public Maintenance create(@Valid @RequestBody MaintenanceRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    public Maintenance update(@PathVariable UUID id, @Valid @RequestBody MaintenanceRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/team/{equipeId}")
    public Maintenance assignTeam(@PathVariable UUID id, @PathVariable UUID equipeId) {
        return service.assignTeam(id, equipeId);
    }

    @PutMapping("/{id}/validate")
    public Maintenance validate(@PathVariable UUID id) { return service.changeStatus(id, "VALIDATED"); }

    @PutMapping("/{id}/reject")
    public Maintenance reject(@PathVariable UUID id) { return service.changeStatus(id, "REJECTED"); }
}
