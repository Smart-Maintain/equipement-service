package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.RapportRequest;
import com.smartmaintain.equipementservice.dto.RapportReviewRequest;
import com.smartmaintain.equipementservice.entities.Maintenance;
import com.smartmaintain.equipementservice.entities.Rapport;
import com.smartmaintain.equipementservice.entities.Tache;
import com.smartmaintain.equipementservice.repositories.MaintenanceRepository;
import com.smartmaintain.equipementservice.repositories.RapportRepository;
import com.smartmaintain.equipementservice.repositories.TacheRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class RapportService {
    private final RapportRepository rapportRepository;
    private final TacheRepository tacheRepository;
    private final MaintenanceRepository maintenanceRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private NotificationService notificationService;

    public RapportService(
            RapportRepository rapportRepository,
            TacheRepository tacheRepository,
            MaintenanceRepository maintenanceRepository) {
        this.rapportRepository = rapportRepository;
        this.tacheRepository = tacheRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    public List<Rapport> getAll() {
        return rapportRepository.findAll();
    }

    public Rapport getById(UUID id) {
        return rapportRepository.findById(id).orElseThrow();
    }

    public List<Rapport> getForRole(String role) {
        String normalizedRole = normalizeRole(role);
        if ("ENGINEER".equals(normalizedRole) || "MANAGER".equals(normalizedRole)) {
            return rapportRepository.findByReviewerRole(normalizedRole);
        }
        return rapportRepository.findByAuthorRole(normalizedRole);
    }

    public List<Rapport> getByStatus(String status) {
        return rapportRepository.findByStatus(status);
    }

    public Rapport create(RapportRequest request) {
        Rapport rapport = new Rapport();
        rapport.setType(normalizeType(request.type()));
        rapport.setStatus("DRAFT");
        rapport.setTitle(request.title());
        rapport.setContent(request.content());
        rapport.setAuthorRole(normalizeRole(request.authorRole()));
        rapport.setAuthorEmail(request.authorEmail());
        if (request.attachments() != null) {
            rapport.setAttachments(request.attachments());
        }

        if (request.tacheId() != null) {
            Tache tache = tacheRepository.findById(request.tacheId()).orElseThrow();
            rapport.setTache(tache);
        }
        if (request.maintenanceId() != null) {
            Maintenance maintenance = maintenanceRepository.findById(request.maintenanceId()).orElseThrow();
            rapport.setMaintenance(maintenance);
        }

        return rapportRepository.save(rapport);
    }

    public Rapport update(UUID id, RapportRequest request) {
        Rapport rapport = getById(id);
        rapport.setTitle(request.title());
        rapport.setContent(request.content());
        if (request.attachments() != null) {
            rapport.setAttachments(request.attachments());
        }
        rapport.setStatus("DRAFT");
        rapport.setReviewerNote(null);
        return rapportRepository.save(rapport);
    }

    public Rapport submit(UUID id) {
        return submit(getById(id));
    }

    public Rapport submit(Rapport rapport) {
        if ("MANAGER".equals(rapport.getAuthorRole())) {
            rapport.setStatus("APPROVED");
            rapport.setReviewerRole("MANAGER");
            LocalDateTime now = LocalDateTime.now();
            rapport.setSubmittedAt(now);
            rapport.setReviewedAt(now);
            Rapport saved = rapportRepository.save(rapport);
            if (notificationService != null) {
                notificationService.sendNotification(null, "ENGINEER", "Job finalized by Manager. Final Report: " + saved.getTitle(), "REPORT", saved.getId());
            }
            return saved;
        }

        rapport.setStatus("SUBMITTED");
        rapport.setReviewerRole("TASK".equals(rapport.getType()) ? "ENGINEER" : "MANAGER");
        rapport.setSubmittedAt(LocalDateTime.now());
        Rapport saved = rapportRepository.save(rapport);

        if (notificationService != null) {
            String reviewerRole = "TASK".equals(saved.getType()) ? "ENGINEER" : "MANAGER";
            notificationService.sendNotification(null, reviewerRole, "New report submitted for review: " + saved.getTitle(), "REPORT", saved.getId());
        }

        return saved;
    }

    public Rapport review(UUID id, RapportReviewRequest request) {
        Rapport rapport = getById(id);
        String status = request.status();
        if (!"APPROVED".equals(status) && !"MODIFICATION_REQUESTED".equals(status)) {
            throw new IllegalArgumentException("Review status must be APPROVED or MODIFICATION_REQUESTED.");
        }
        rapport.setStatus(status);
        rapport.setReviewerNote(request.reviewerNote());
        rapport.setReviewedAt(LocalDateTime.now());
        return rapportRepository.save(rapport);
    }

    private String normalizeType(String type) {
        String normalized = type == null ? "TASK" : type.toUpperCase(Locale.ROOT);
        if (!"TASK".equals(normalized) && !"ASSIGNMENT".equals(normalized)) {
            throw new IllegalArgumentException("Rapport type must be TASK or ASSIGNMENT.");
        }
        return normalized;
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "USER";
        }
        return role.toUpperCase(Locale.ROOT).replace("TECHNICIAN", "TECHNICIAN");
    }
}
