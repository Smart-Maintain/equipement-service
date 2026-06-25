package com.smartmaintain.equipementservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;
    private String status;

    private UUID assignedMemberId;
    private String assignedMemberName;

    @ManyToOne
    @JoinColumn(name = "tache_id")
    @JsonIgnore
    private Tache tache;

    public SubTask() {}

    public SubTask(String description, String status, UUID assignedMemberId, String assignedMemberName, Tache tache) {
        this.description = description;
        this.status = status;
        this.assignedMemberId = assignedMemberId;
        this.assignedMemberName = assignedMemberName;
        this.tache = tache;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public UUID getAssignedMemberId() { return assignedMemberId; }
    public void setAssignedMemberId(UUID assignedMemberId) { this.assignedMemberId = assignedMemberId; }

    public String getAssignedMemberName() { return assignedMemberName; }
    public void setAssignedMemberName(String assignedMemberName) { this.assignedMemberName = assignedMemberName; }

    public Tache getTache() { return tache; }
    public void setTache(Tache tache) { this.tache = tache; }
}
