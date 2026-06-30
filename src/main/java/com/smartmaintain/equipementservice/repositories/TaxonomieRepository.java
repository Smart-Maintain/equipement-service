package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.Taxonomie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomieRepository extends JpaRepository<Taxonomie, Long> {
}
