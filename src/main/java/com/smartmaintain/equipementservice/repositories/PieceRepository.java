package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.Piece;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PieceRepository extends JpaRepository<Piece, UUID> {
    List<Piece> findByTaxonomieId(Long taxonomieId);
}
