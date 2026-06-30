package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.UUID;

public interface TacheRepository extends JpaRepository<Tache, UUID> {
    @EntityGraph(attributePaths = {"equipe", "taxonomie", "maintenance"})
    List<Tache> findAll();

    @EntityGraph(attributePaths = {"equipe", "taxonomie", "maintenance"})
    List<Tache> findByTaxonomieId(Long taxonomieId);

    @EntityGraph(attributePaths = {"equipe", "taxonomie", "maintenance"})
    List<Tache> findByMaintenanceId(UUID maintenanceId);
}
