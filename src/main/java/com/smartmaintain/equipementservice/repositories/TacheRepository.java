package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TacheRepository extends JpaRepository<Tache, UUID> {
    List<Tache> findByTaxonomieId(Long taxonomieId);
    List<Tache> findByMaintenanceId(UUID maintenanceId);
}
