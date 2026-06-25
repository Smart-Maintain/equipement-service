package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.TacheRequest;
import com.smartmaintain.equipementservice.entities.Equipe;
import com.smartmaintain.equipementservice.entities.Maintenance;
import com.smartmaintain.equipementservice.entities.Tache;
import com.smartmaintain.equipementservice.entities.Taxonomie;
import com.smartmaintain.equipementservice.repositories.EquipeRepository;
import com.smartmaintain.equipementservice.repositories.MaintenanceRepository;
import com.smartmaintain.equipementservice.repositories.TaxonomieRepository;
import com.smartmaintain.equipementservice.repositories.TacheRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TacheService {
    private final TacheRepository tacheRepository;
    private final  EquipeRepository equipeRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final TaxonomieRepository taxonomieRepository;
    // Constructor injection...
    @Autowired
    public TacheService(
            TacheRepository tacheRepository,
            EquipeRepository equipeRepository,
            MaintenanceRepository maintenanceRepository,
            TaxonomieRepository taxonomieRepository) {
        this.tacheRepository = tacheRepository;
        this.equipeRepository = equipeRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.taxonomieRepository = taxonomieRepository;
    }

    public TacheService(
            TacheRepository tacheRepository,
            EquipeRepository equipeRepository,
            MaintenanceRepository maintenanceRepository) {
        this.tacheRepository = tacheRepository;
        this.equipeRepository = equipeRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.taxonomieRepository = null;
    }

    @Autowired
    private NotificationService notificationService;

    public void assignTaskToTeam(UUID tacheId, UUID equipeId) {
        Tache tache = tacheRepository.findById(tacheId).orElseThrow();
        Equipe equipe = equipeRepository.findById(equipeId).orElseThrow();
        tache.setEquipe(equipe);
        Tache saved = tacheRepository.save(tache);
        if (notificationService != null) {
            notificationService.sendNotification(null, "TECHNICIAN", "Task assigned to team: " + equipe.getNom() + " - " + saved.getDescription(), "TASK", saved.getId());
        }
    }

    public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
    }

    public List<Tache> getTachesByTaxonomie(Long taxonomieId) {
        return tacheRepository.findByTaxonomieId(taxonomieId);
    }

    public List<Tache> getTachesByMaintenance(UUID maintenanceId) {
        return tacheRepository.findByMaintenanceId(maintenanceId);
    }
    public Tache getTacheById(UUID id) {
        return tacheRepository.findById(id).orElse(null);
    }

    public Tache saveTache(Tache tache) {
        return tacheRepository.save(tache);
    }

    public Tache createTask(TacheRequest request) {
        Tache tache = new Tache();
        applyRequest(tache, request);
        if (tache.getStatus() == null) {
            tache.setStatus("pending");
        }
        Tache saved = tacheRepository.save(tache);
        if (notificationService != null) {
            notificationService.sendNotification(null, "TECHNICIAN", "New task assigned: " + saved.getDescription(), "TASK", saved.getId());
            notificationService.sendNotification(null, "ENGINEER", "New task created: " + saved.getDescription(), "TASK", saved.getId());
        }
        return saved;
    }

    public Tache updateTask(UUID id, TacheRequest request) {
        Tache tache = getTacheById(id);
        if (tache == null) {
            throw new IllegalArgumentException("Task not found: " + id);
        }
        applyRequest(tache, request);
        return tacheRepository.save(tache);
    }

    public void deleteTask(UUID id) {
        tacheRepository.deleteById(id);
    }

    public Tache addTechnicianNote(UUID id, String note) {
        Tache tache = getTacheById(id);
        if (tache == null) {
            throw new IllegalArgumentException("Task not found: " + id);
        }
        tache.setTechnicianNote(note);
        return tacheRepository.save(tache);
    }

    public Tache checkTaskDone(UUID id) {
        Tache tache = getTacheById(id);
        if (tache == null) {
            throw new IllegalArgumentException("Task not found: " + id);
        }
        tache.setStatus("completed");
        tache.setCheckedAt(LocalDateTime.now());

        double totalCost = 0.0;
        if (tache.getPieceRequests() != null) {
            for (com.smartmaintain.equipementservice.entities.PieceRequest pr : tache.getPieceRequests()) {
                if ("APPROVED".equals(pr.getStatus())) {
                    totalCost += pr.getQuantite() * pr.getPiece().getPrix();
                }
            }
        }
        tache.setTotalCost(totalCost);

        return tacheRepository.save(tache);
    }

    public Tache attachToMaintenance(UUID taskId, UUID maintenanceId) {
        Tache tache = getTacheById(taskId);
        if (tache == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId).orElseThrow();
        tache.setMaintenance(maintenance);
        return tacheRepository.save(tache);
    }

    public void unassignTaskFromTeam(UUID tacheId) {
        Tache tache = tacheRepository.findById(tacheId).orElseThrow();
        tache.setEquipe(null);
        tacheRepository.save(tache);
    }

    private void applyRequest(Tache tache, TacheRequest request) {
        tache.setDescription(request.description());
        tache.setPriorite(request.priorite());
        tache.setStatus(request.status());

        if (request.equipeId() != null) {
            Equipe equipe = equipeRepository.findById(request.equipeId()).orElseThrow();
            tache.setEquipe(equipe);
        } else {
            tache.setEquipe(null);
        }
        
        if (request.maintenanceId() != null) {
            Maintenance maintenance = maintenanceRepository.findById(request.maintenanceId()).orElseThrow();
            tache.setMaintenance(maintenance);
        }
        if (request.taxonomieId() != null && taxonomieRepository != null) {
            Taxonomie taxonomie = taxonomieRepository.findById(request.taxonomieId()).orElseThrow();
            tache.setTaxonomie(taxonomie);
        }

        // Handle subtasks
        if (request.subTasks() != null) {
            tache.getSubTasks().clear();
            for (com.smartmaintain.equipementservice.dto.SubTaskRequest str : request.subTasks()) {
                com.smartmaintain.equipementservice.entities.SubTask subTask = new com.smartmaintain.equipementservice.entities.SubTask(
                        str.description(),
                        str.status(),
                        str.assignedMemberId(),
                        str.assignedMemberName(),
                        tache
                );
                tache.getSubTasks().add(subTask);
            }
        }
    }
}
