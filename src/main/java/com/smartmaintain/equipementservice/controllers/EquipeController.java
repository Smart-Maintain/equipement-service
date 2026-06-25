package com.smartmaintain.equipementservice.controllers;

import com.smartmaintain.equipementservice.dto.EquipeRequest;
import com.smartmaintain.equipementservice.entities.Equipe;
import com.smartmaintain.equipementservice.services.EquipeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/equipes")
public class EquipeController {
    private final EquipeService service;

    public EquipeController(EquipeService service) { this.service = service; }

    @GetMapping
    public List<Equipe> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Equipe getById(@PathVariable UUID id) { return service.getById(id); }

    @PostMapping
    public Equipe create(@RequestBody EquipeRequest request) { return service.createTeam(request); }

    @PutMapping("/{id}")
    public Equipe update(@PathVariable UUID id, @RequestBody EquipeRequest request) {
        return service.updateTeam(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) { service.delete(id); }
}
