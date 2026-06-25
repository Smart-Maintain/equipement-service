package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.PieceRequestDto;
import com.smartmaintain.equipementservice.entities.Piece;
import com.smartmaintain.equipementservice.entities.PieceRequest;
import com.smartmaintain.equipementservice.entities.Tache;
import com.smartmaintain.equipementservice.repositories.PieceRepository;
import com.smartmaintain.equipementservice.repositories.PieceRequestRepository;
import com.smartmaintain.equipementservice.repositories.TacheRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PieceRequestService {

    private final PieceRequestRepository pieceRequestRepository;
    private final PieceRepository pieceRepository;
    private final TacheRepository tacheRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    public PieceRequestService(PieceRequestRepository pieceRequestRepository,
                               PieceRepository pieceRepository,
                               TacheRepository tacheRepository) {
        this.pieceRequestRepository = pieceRequestRepository;
        this.pieceRepository = pieceRepository;
        this.tacheRepository = tacheRepository;
    }

    public PieceRequest createRequest(PieceRequestDto dto) {
        Tache tache = tacheRepository.findById(dto.tacheId())
                .orElseThrow(() -> new RuntimeException("Tache not found"));
        Piece piece = pieceRepository.findById(dto.pieceId())
                .orElseThrow(() -> new RuntimeException("Piece not found"));

        PieceRequest request = PieceRequest.builder()
                .tache(tache)
                .piece(piece)
                .quantite(dto.quantite())
                .status("PENDING")
                .requestedBy(dto.requestedBy())
                .createdAt(LocalDateTime.now())
                .build();

        return pieceRequestRepository.save(request);
    }

    public List<PieceRequest> getAllRequests() {
        return pieceRequestRepository.findAll();
    }

    public List<PieceRequest> getPendingRequests() {
        return pieceRequestRepository.findByStatus("PENDING");
    }

    public List<PieceRequest> getRequestsByTache(UUID tacheId) {
        return pieceRequestRepository.findByTacheId(tacheId);
    }

    public PieceRequest approveRequest(UUID requestId) {
        PieceRequest request = pieceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("PieceRequest not found"));
        
        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Request is not PENDING");
        }

        Piece piece = request.getPiece();
        if (piece.getQuantite() < request.getQuantite()) {
            throw new RuntimeException("Not enough stock for piece " + piece.getNom());
        }

        // Deduct stock
        piece.setQuantite(piece.getQuantite() - request.getQuantite());
        pieceRepository.save(piece);

        request.setStatus("APPROVED");
        request.setRespondedAt(LocalDateTime.now());
        
        // Notify admin if low stock
        if (piece.getQuantite() <= piece.getSeuilMin() && notificationService != null) {
            notificationService.sendNotification(null, "ADMIN", "Stock faible pour la piece: " + piece.getNom() + " (Restant: " + piece.getQuantite() + ")", "STOCK", piece.getId());
        }

        return pieceRequestRepository.save(request);
    }

    public PieceRequest rejectRequest(UUID requestId) {
        PieceRequest request = pieceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("PieceRequest not found"));

        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Request is not PENDING");
        }

        request.setStatus("REJECTED");
        request.setRespondedAt(LocalDateTime.now());
        
        return pieceRequestRepository.save(request);
    }
}
