package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    List<Equipement> findByTaxonomieCode(String code);
}