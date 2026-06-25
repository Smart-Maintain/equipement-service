package com.smartmaintain.equipementservice.controllers;

import com.smartmaintain.equipementservice.dto.TacheRequest;
import com.smartmaintain.equipementservice.dto.TechnicianNoteRequest;
import com.smartmaintain.equipementservice.entities.Tache;
import com.smartmaintain.equipementservice.services.TacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/taches")
public class TacheController {
    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping("/filter/{taxonomieId}")
    public ResponseEntity<List<Tache>> filterByTaxonomie(@PathVariable Long taxonomieId) {
        return ResponseEntity.ok(tacheService.getTachesByTaxonomie(taxonomieId));
    }

    @GetMapping
    public ResponseEntity<List<Tache>> getAllTaches() {
        return ResponseEntity.ok(tacheService.getAllTaches());
    }

    @GetMapping("/maintenance/{maintenanceId}")
    public ResponseEntity<List<Tache>> getByMaintenance(@PathVariable java.util.UUID maintenanceId) {
        return ResponseEntity.ok(tacheService.getTachesByMaintenance(maintenanceId));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/status/{status}")
    public ResponseEntity<Tache> updateTacheStatus(@PathVariable java.util.UUID id, @PathVariable String status) {
        Tache tache = tacheService.getTacheById(id);
        if (tache != null) {
            tache.setStatus(status);
            return ResponseEntity.ok(tacheService.saveTache(tache));
        }
        return ResponseEntity.notFound().build();
    }

    @org.springframework.web.bind.annotation.PostMapping
    public ResponseEntity<Tache> createTache(@org.springframework.web.bind.annotation.RequestBody TacheRequest request) {
        return ResponseEntity.ok(tacheService.createTask(request));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<Tache> updateTache(
            @PathVariable java.util.UUID id,
            @org.springframework.web.bind.annotation.RequestBody TacheRequest request) {
        return ResponseEntity.ok(tacheService.updateTask(id, request));
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTache(@PathVariable java.util.UUID id) {
        tacheService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/note")
    public ResponseEntity<Tache> addTechnicianNote(
            @PathVariable java.util.UUID id,
            @org.springframework.web.bind.annotation.RequestBody TechnicianNoteRequest request) {
        return ResponseEntity.ok(tacheService.addTechnicianNote(id, request.note()));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/check")
    public ResponseEntity<Tache> checkTask(@PathVariable java.util.UUID id) {
        return ResponseEntity.ok(tacheService.checkTaskDone(id));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/maintenance/{maintenanceId}")
    public ResponseEntity<Tache> attachToMaintenance(
            @PathVariable java.util.UUID id,
            @PathVariable java.util.UUID maintenanceId) {
        return ResponseEntity.ok(tacheService.attachToMaintenance(id, maintenanceId));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/assign-team/{teamId}")
    public ResponseEntity<Void> assignToTeam(
            @PathVariable java.util.UUID id,
            @PathVariable java.util.UUID teamId) {
        tacheService.assignTaskToTeam(id, teamId);
        return ResponseEntity.ok().build();
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/unassign-team")
    public ResponseEntity<Void> unassignFromTeam(@PathVariable java.util.UUID id) {
        tacheService.unassignTaskFromTeam(id);
        return ResponseEntity.ok().build();
    }
}
