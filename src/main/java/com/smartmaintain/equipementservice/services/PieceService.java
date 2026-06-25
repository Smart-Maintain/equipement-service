package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.entities.Piece;
import com.smartmaintain.equipementservice.repositories.PieceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class PieceService {

    private final PieceRepository pieceRepository;

    public PieceService(PieceRepository pieceRepository) {
        this.pieceRepository = pieceRepository;
    }

    public List<Piece> getAllPieces() {
        return pieceRepository.findAll();
    }

    public Piece getPieceById(UUID id) {
        return pieceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piece introuvable"));
    }

    public Piece createPiece(Piece piece) {
        return pieceRepository.save(piece);
    }

    public List<Piece> getPiecesByTaxonomie(Long taxonomieId) {
        return pieceRepository.findByTaxonomieId(taxonomieId);
    }

    public Piece updatePiece(UUID id, Piece newPiece) {
        return pieceRepository.findById(id).map(piece -> {
            piece.setNom(newPiece.getNom());
            piece.setReference(newPiece.getReference());
            piece.setPrix(newPiece.getPrix());
            piece.setQuantite(newPiece.getQuantite());
            piece.setEmplacement(newPiece.getEmplacement());
            piece.setSeuilMin(newPiece.getSeuilMin());
            piece.setTaxonomie(newPiece.getTaxonomie());
            return pieceRepository.save(piece);
        }).orElseThrow(() -> new RuntimeException("Piece introuvable avec l'id: " + id));
    }

    public void deletePiece(UUID id) {
        pieceRepository.deleteById(id);
    }

    public Piece updateQuantite(UUID id, int nouvelleQuantite) {
        Piece piece = pieceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Piece introuvable"));
        piece.setQuantite(nouvelleQuantite);
        return pieceRepository.save(piece);
    }
}