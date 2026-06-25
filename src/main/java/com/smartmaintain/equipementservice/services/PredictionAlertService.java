package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.PredictionAlertRequest;
import com.smartmaintain.equipementservice.entities.Equipe;
import com.smartmaintain.equipementservice.entities.Equipement;
import com.smartmaintain.equipementservice.entities.PredictionAlert;
import com.smartmaintain.equipementservice.entities.Tache;
import com.smartmaintain.equipementservice.entities.Taxonomie;
import com.smartmaintain.equipementservice.repositories.EquipeRepository;
import com.smartmaintain.equipementservice.repositories.EquipementRepository;
import com.smartmaintain.equipementservice.repositories.PredictionAlertRepository;
import com.smartmaintain.equipementservice.repositories.TacheRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PredictionAlertService {

    private final PredictionAlertRepository alertRepository;
    private final TacheRepository tacheRepository;
    private final EquipeRepository equipeRepository;
    private final EquipementRepository equipementRepository;

    public PredictionAlertService(PredictionAlertRepository alertRepository, TacheRepository tacheRepository, EquipeRepository equipeRepository, EquipementRepository equipementRepository) {
        this.alertRepository = alertRepository;
        this.tacheRepository = tacheRepository;
        this.equipeRepository = equipeRepository;
        this.equipementRepository = equipementRepository;
    }

    public PredictionAlert createAlert(PredictionAlertRequest request) {
        PredictionAlert alert = PredictionAlert.builder()
                .engineId(request.engineId())
                .engineName(request.engineName())
                .message(request.message())
                .type(request.type())
                .timestamp(LocalDateTime.now())
                .status("PENDING")
                .build();
        return alertRepository.save(alert);
    }

    public List<PredictionAlert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Tache convertToTask(Long alertId, UUID equipeId) {
        PredictionAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
                
        if ("CONVERTED".equals(alert.getStatus())) {
            throw new RuntimeException("Alert is already converted to a task");
        }

        Equipe equipe = null;
        if (equipeId != null) {
            equipe = equipeRepository.findById(equipeId).orElse(null);
        }

        Taxonomie taxonomie = null;
        if (alert.getEngineId() != null) {
            Equipement eq = equipementRepository.findById(alert.getEngineId()).orElse(null);
            if (eq != null) {
                taxonomie = eq.getTaxonomie();
            }
        }

        Tache tache = new Tache();
        tache.setDescription(alert.getMessage());
        tache.setPriorite(alert.getType() != null ? alert.getType() : "high");
        tache.setStatus("pending");
        tache.setEquipe(equipe);
        tache.setTaxonomie(taxonomie);
        
        Tache savedTache = tacheRepository.save(tache);
        
        alert.setStatus("CONVERTED");
        alertRepository.save(alert);
        
        return savedTache;
    }
}
