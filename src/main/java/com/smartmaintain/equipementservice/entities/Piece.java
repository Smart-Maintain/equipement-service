package com.smartmaintain.equipementservice.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String reference;

    @Column(nullable = false)
    private String nom;

    private int quantite;
    private double prix;

    private String emplacement;
    private int seuilMin;
    @ManyToOne
    @JoinColumn(name = "taxonomie_id")
    private Taxonomie taxonomie;
}