package com.smartmaintain.equipementservice.controllers;

import com.smartmaintain.equipementservice.entities.Equipement;
import com.smartmaintain.equipementservice.services.EquipementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/equipements")
public class EquipementController {
    private final EquipementService service;

    public EquipementController(EquipementService service) {
        this.service = service;
    }

    @GetMapping
    public List<Equipement> getAll() { return service.getAll(); }

    @GetMapping("/type/{code}")
    public List<Equipement> getByTypeCode(@PathVariable String code) {
        return service.getByTaxonomy(code.toUpperCase());
    }

    @PostMapping
    public Equipement create(@RequestBody Equipement e) {
        return service.save(e);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipement> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public Equipement update(@PathVariable Long id, @RequestBody Equipement e) {
        e.setId(id);
        return service.save(e);
    }
}