package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.PredictionAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionAlertRepository extends JpaRepository<PredictionAlert, Long> {
}
