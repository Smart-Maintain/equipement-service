package com.smartmaintain.equipementservice.controllers;

import jakarta.validation.Valid;

import com.smartmaintain.equipementservice.dto.PieceRequestDto;
import com.smartmaintain.equipementservice.dto.PieceRequestStatusDto;
import com.smartmaintain.equipementservice.entities.PieceRequest;
import com.smartmaintain.equipementservice.services.PieceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/piece-requests")
public class PieceRequestController {

    private final PieceRequestService pieceRequestService;

    @Autowired
    public PieceRequestController(PieceRequestService pieceRequestService) {
        this.pieceRequestService = pieceRequestService;
    }

    @PostMapping
    public ResponseEntity<PieceRequest> createRequest(@Valid @RequestBody PieceRequestDto dto) {
        return ResponseEntity.ok(pieceRequestService.createRequest(dto));
    }

    @GetMapping
    public ResponseEntity<List<PieceRequest>> getAllRequests() {
        return ResponseEntity.ok(pieceRequestService.getAllRequests());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PieceRequest>> getPendingRequests() {
        return ResponseEntity.ok(pieceRequestService.getPendingRequests());
    }

    @GetMapping("/tache/{tacheId}")
    public ResponseEntity<List<PieceRequest>> getRequestsByTache(@PathVariable UUID tacheId) {
        return ResponseEntity.ok(pieceRequestService.getRequestsByTache(tacheId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PieceRequest> updateStatus(@PathVariable UUID id, @Valid @RequestBody PieceRequestStatusDto dto) {
        if ("APPROVED".equalsIgnoreCase(dto.status())) {
            return ResponseEntity.ok(pieceRequestService.approveRequest(id));
        } else if ("REJECTED".equalsIgnoreCase(dto.status())) {
            return ResponseEntity.ok(pieceRequestService.rejectRequest(id));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
