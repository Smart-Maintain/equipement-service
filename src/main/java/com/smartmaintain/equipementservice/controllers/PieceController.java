package com.smartmaintain.equipementservice.controllers;

import com.smartmaintain.equipementservice.entities.Piece;
import com.smartmaintain.equipementservice.services.PieceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pieces")
public class PieceController {

    private final PieceService pieceService;

    public PieceController(PieceService pieceService) {
        this.pieceService = pieceService;
    }

    @GetMapping
    public ResponseEntity<List<Piece>> getAllPieces() {
        return ResponseEntity.ok(pieceService.getAllPieces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Piece> getPieceById(@PathVariable UUID id) {
        return ResponseEntity.ok(pieceService.getPieceById(id));
    }

    @PostMapping
    public ResponseEntity<Piece> createPiece(@RequestBody Piece piece) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pieceService.createPiece(piece));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Piece> updatePiece(@PathVariable UUID id, @RequestBody Piece piece) {
        return ResponseEntity.ok(pieceService.updatePiece(id, piece));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePiece(@PathVariable UUID id) {
        pieceService.deletePiece(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter/{taxonomieId}")
    public ResponseEntity<List<Piece>> filterByTaxonomie(@PathVariable Long taxonomieId) {
        return ResponseEntity.ok(pieceService.getPiecesByTaxonomie(taxonomieId));
    }

    @PutMapping("/{id}/quantite")
    public ResponseEntity<Piece> updateQuantite(@PathVariable UUID id, @RequestParam int nouvelleQuantite) {
        return ResponseEntity.ok(pieceService.updateQuantite(id, nouvelleQuantite));
    }
}