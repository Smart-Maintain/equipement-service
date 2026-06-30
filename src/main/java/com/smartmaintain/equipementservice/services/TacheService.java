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
    private final EquipeRepository equipeRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final TaxonomieRepository taxonomieRepository;
    
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

    public List<Tache> getMyTasks(String role, String username) {
        List<Tache> allTasks = tacheRepository.findAll();
        if (username == null) return allTasks;
        
        String normalizedUsername = username.replaceAll("\\s", "").toLowerCase();
        
        if ("engineer".equalsIgnoreCase(role) || "ingenieur".equalsIgnoreCase(role)) {
            return allTasks.stream()
                .filter(t -> t.getEquipe() != null && t.getEquipe().getLeaderEngineerName() != null && t.getEquipe().getLeaderEngineerName().replaceAll("\\s", "").toLowerCase().contains(normalizedUsername))
                .toList();
        } else if ("technician".equalsIgnoreCase(role) || "operateur".equalsIgnoreCase(role)) {
            return allTasks.stream()
                .filter(t -> t.getEquipe() != null && t.getEquipe().getTechnicianNames() != null && t.getEquipe().getTechnicianNames().stream().anyMatch(n -> n != null && n.replaceAll("\\s", "").toLowerCase().contains(normalizedUsername)))
                .toList();
        }
        
        return allTasks;
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
                        (str.status() == null || str.status().trim().isEmpty()) ? "PENDING" : str.status(),
                        str.assignedMemberId(),
                        str.assignedMemberName(),
                        tache
                );
                tache.getSubTasks().add(subTask);
            }
        }
    }

    public Tache updateSubTaskStatus(UUID taskId, UUID subTaskId, String status, String username) {
        Tache tache = getTacheById(taskId);
        if (tache == null) {
            throw new IllegalArgumentException("Task not found");
        }

        // Find the subtask first to check if it's unassigned
        com.smartmaintain.equipementservice.entities.SubTask targetSubTask = null;
        for (com.smartmaintain.equipementservice.entities.SubTask st : tache.getSubTasks()) {
            if (st.getId().equals(subTaskId)) {
                targetSubTask = st;
                break;
            }
        }
        if (targetSubTask == null) {
            throw new IllegalArgumentException("SubTask not found");
        }

        // Unassigned subtasks: anyone can change status
        boolean isUnassigned = targetSubTask.getAssignedMemberId() == null
                && (targetSubTask.getAssignedMemberName() == null || targetSubTask.getAssignedMemberName().isEmpty());

        if (!isUnassigned) {
            // For assigned subtasks, check team membership
            boolean isAuthorized = false;
            if (tache.getEquipe() != null) {
                if (username.equals(tache.getEquipe().getLeaderEngineerName())) {
                    isAuthorized = true;
                } else {
                    Equipe equipe = equipeRepository.findById(tache.getEquipe().getId()).orElse(null);
                    if (equipe != null && equipe.getTechnicianNames() != null && equipe.getTechnicianNames().contains(username)) {
                        isAuthorized = true;
                    }
                }
            }
            if (!isAuthorized) {
                throw new RuntimeException("Unauthorized: Only assigned team members can edit subtask status");
            }
        }

        targetSubTask.setStatus(status);
        return tacheRepository.save(tache);
    }

    public Tache addSubTaskToTask(UUID taskId, String description, UUID assignedMemberId, String assignedMemberName) {
        Tache tache = getTacheById(taskId);
        if (tache == null) {
            throw new IllegalArgumentException("Task not found");
        }
        com.smartmaintain.equipementservice.entities.SubTask subTask = new com.smartmaintain.equipementservice.entities.SubTask(
                description,
                "PENDING",
                assignedMemberId,
                assignedMemberName,
                tache
        );
        tache.getSubTasks().add(subTask);
        return tacheRepository.save(tache);
    }
}
