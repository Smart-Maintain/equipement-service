package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RapportRepository extends JpaRepository<Rapport, UUID> {
    List<Rapport> findByStatus(String status);
    List<Rapport> findByReviewerRole(String reviewerRole);
    List<Rapport> findByAuthorRole(String authorRole);
    List<Rapport> findByTacheId(UUID tacheId);
    List<Rapport> findByMaintenanceId(UUID maintenanceId);
}
