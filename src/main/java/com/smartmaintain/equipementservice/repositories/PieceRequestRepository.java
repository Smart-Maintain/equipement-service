package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.PieceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PieceRequestRepository extends JpaRepository<PieceRequest, UUID> {
    List<PieceRequest> findByTacheId(UUID tacheId);
    List<PieceRequest> findByStatus(String status);
}
