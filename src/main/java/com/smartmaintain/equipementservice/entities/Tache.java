package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String description;
    private String priorite;
    private String status;
    @Column(length = 2000)
    private String technicianNote;
    private LocalDateTime checkedAt;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    @ManyToOne
    @JoinColumn(name = "maintenance_id")
    private Maintenance maintenance;

    public Tache() {}

    public Tache(UUID id, String description, String priorite, String status, Equipe equipe) {
        this.id = id;
        this.description = description;
        this.priorite = priorite;
        this.status = status;
        this.equipe = equipe;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public String getTechnicianNote() {
        return technicianNote;
    }

    public void setTechnicianNote(String technicianNote) {
        this.technicianNote = technicianNote;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }
    @ManyToOne
    @JoinColumn(name = "taxonomie_id")
    private Taxonomie taxonomie;

    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<SubTask> subTasks = new java.util.ArrayList<>();

    public Taxonomie getTaxonomie() {
        return taxonomie;
    }

    public void setTaxonomie(Taxonomie taxonomie) {
        this.taxonomie = taxonomie;
    }

    public java.util.List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(java.util.List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    private Double totalCost;

    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("tache")
    private java.util.List<PieceRequest> pieceRequests = new java.util.ArrayList<>();

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public java.util.List<PieceRequest> getPieceRequests() {
        return pieceRequests;
    }

    public void setPieceRequests(java.util.List<PieceRequest> pieceRequests) {
        this.pieceRequests = pieceRequests;
    }
}
