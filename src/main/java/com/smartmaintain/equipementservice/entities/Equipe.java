package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nom;
    private String specialite;
    private UUID leaderEngineerId;
    private String leaderEngineerName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "equipe_techniciens", joinColumns = @JoinColumn(name = "equipe_id"))
    @Column(name = "technician_id")
    private List<UUID> technicianIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "equipe_technicien_names", joinColumns = @JoinColumn(name = "equipe_id"))
    @Column(name = "technician_name")
    private List<String> technicianNames = new ArrayList<>();

    public Equipe() {}

    public Equipe(UUID id, String nom, String specialite) {
        this.id = id;
        this.nom = nom;
        this.specialite = specialite;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public UUID getLeaderEngineerId() {
        return leaderEngineerId;
    }

    public void setLeaderEngineerId(UUID leaderEngineerId) {
        this.leaderEngineerId = leaderEngineerId;
    }

    public String getLeaderEngineerName() {
        return leaderEngineerName;
    }

    public void setLeaderEngineerName(String leaderEngineerName) {
        this.leaderEngineerName = leaderEngineerName;
    }

    public List<UUID> getTechnicianIds() {
        return technicianIds;
    }

    public void setTechnicianIds(List<UUID> technicianIds) {
        this.technicianIds = technicianIds == null ? new ArrayList<>() : technicianIds;
    }

    public List<String> getTechnicianNames() {
        return technicianNames;
    }

    public void setTechnicianNames(List<String> technicianNames) {
        this.technicianNames = technicianNames == null ? new ArrayList<>() : technicianNames;
    }
}
