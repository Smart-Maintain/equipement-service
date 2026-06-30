package com.smartmaintain.equipementservice.controllers;

import jakarta.validation.Valid;

import com.smartmaintain.equipementservice.dto.RapportRequest;
import com.smartmaintain.equipementservice.dto.RapportReviewRequest;
import com.smartmaintain.equipementservice.entities.Rapport;
import com.smartmaintain.equipementservice.services.RapportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rapports")
public class RapportController {
    private final RapportService rapportService;

    public RapportController(RapportService rapportService) {
        this.rapportService = rapportService;
    }

    @GetMapping
    public List<Rapport> getAll() {
        return rapportService.getAll();
    }

    @GetMapping("/{id}")
    public Rapport getById(@PathVariable UUID id) {
        return rapportService.getById(id);
    }

    @GetMapping("/role/{role}")
    public List<Rapport> getForRole(@PathVariable String role) {
        return rapportService.getForRole(role);
    }

    @GetMapping("/status/{status}")
    public List<Rapport> getByStatus(@PathVariable String status) {
        return rapportService.getByStatus(status);
    }

    @PostMapping
    public Rapport create(@Valid @RequestBody RapportRequest request) {
        return rapportService.create(request);
    }

    @PutMapping("/{id}")
    public Rapport update(@PathVariable UUID id, @Valid @RequestBody RapportRequest request) {
        return rapportService.update(id, request);
    }

    @PostMapping("/{id}/submit")
    public Rapport submit(@PathVariable UUID id) {
        return rapportService.submit(id);
    }

    @PostMapping("/{id}/review")
    public Rapport review(@PathVariable UUID id, @Valid @RequestBody RapportReviewRequest request) {
        return rapportService.review(id, request);
    }
}
