package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.MaintenanceRequest;
import com.smartmaintain.equipementservice.entities.Equipe;
import com.smartmaintain.equipementservice.entities.Equipement;
import com.smartmaintain.equipementservice.entities.Maintenance;
import com.smartmaintain.equipementservice.repositories.EquipeRepository;
import com.smartmaintain.equipementservice.repositories.EquipementRepository;
import com.smartmaintain.equipementservice.repositories.MaintenanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class MaintenanceService {
    private final MaintenanceRepository repository;
    private final EquipementRepository equipementRepository;
    private final EquipeRepository equipeRepository;

    public MaintenanceService(
            MaintenanceRepository repository,
            EquipementRepository equipementRepository,
            EquipeRepository equipeRepository) {
        this.repository = repository;
        this.equipementRepository = equipementRepository;
        this.equipeRepository = equipeRepository;
    }

    public List<Maintenance> getAll() { return repository.findAll(); }
    public Maintenance getById(UUID id) { return repository.findById(id).orElseThrow(); }
    public Maintenance save(Maintenance m) { return repository.save(m); }

    public Maintenance create(MaintenanceRequest request) {
        Maintenance maintenance = new Maintenance();
        applyRequest(maintenance, request);
        if (maintenance.getStatus() == null) {
            maintenance.setStatus("IN_PROGRESS");
        }
        return repository.save(maintenance);
    }

    public Maintenance update(UUID id, MaintenanceRequest request) {
        Maintenance maintenance = getById(id);
        applyRequest(maintenance, request);
        return repository.save(maintenance);
    }

    public Maintenance assignTeam(UUID id, UUID equipeId) {
        Maintenance maintenance = getById(id);
        Equipe equipe = equipeRepository.findById(equipeId).orElseThrow();
        maintenance.setEquipe(equipe);
        return repository.save(maintenance);
    }

    public Maintenance changeStatus(UUID id, String status) {
        Maintenance m = getById(id);
        m.setStatus(status);
        return repository.save(m);
    }

    private void applyRequest(Maintenance maintenance, MaintenanceRequest request) {
        maintenance.setDescription(request.description());
        maintenance.setStatus(request.status());
        maintenance.setDateDebut(request.dateDebut());
        maintenance.setDateFin(request.dateFin());
        maintenance.setTypeMaintenance(request.typeMaintenance());

        if (request.equipementId() != null) {
            Equipement equipement = equipementRepository.findById(request.equipementId()).orElseThrow();
            maintenance.setEquipement(equipement);
        }
        if (request.equipeId() != null) {
            Equipe equipe = equipeRepository.findById(request.equipeId()).orElseThrow();
            maintenance.setEquipe(equipe);
        }
    }
}
