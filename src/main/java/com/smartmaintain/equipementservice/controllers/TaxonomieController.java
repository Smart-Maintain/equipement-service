package com.smartmaintain.equipementservice.controllers;

import com.smartmaintain.equipementservice.entities.Taxonomie;
import com.smartmaintain.equipementservice.services.TaxonomieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taxonomies")
public class TaxonomieController {

    private final TaxonomieService taxonomieService;

    public TaxonomieController(TaxonomieService taxonomieService) {
        this.taxonomieService = taxonomieService;
    }

    @GetMapping
    public List<Taxonomie> getAllTaxonomies() {
        return taxonomieService.getAllTaxonomies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taxonomie> getTaxonomieById(@PathVariable Long id) {
        return taxonomieService.getTaxonomieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Taxonomie createTaxonomie(@RequestBody Taxonomie taxonomie) {
        return taxonomieService.saveTaxonomie(taxonomie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Taxonomie> updateTaxonomie(@PathVariable Long id, @RequestBody Taxonomie taxonomieDetails) {
        return ResponseEntity.ok(taxonomieService.updateTaxonomie(id, taxonomieDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxonomie(@PathVariable Long id) {
        taxonomieService.deleteTaxonomie(id);
        return ResponseEntity.noContent().build();
    }
}