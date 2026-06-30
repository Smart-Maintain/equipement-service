package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true, nullable = false)
    private String reference;

    private String model;
    private String status; // 'En service', 'En maintenance', etc.

    @ManyToOne
    @JoinColumn(name = "taxonomie_id", nullable = false)
    private Taxonomie taxonomie;
}