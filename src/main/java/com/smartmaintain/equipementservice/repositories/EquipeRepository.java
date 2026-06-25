package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EquipeRepository extends JpaRepository<Equipe, UUID> {
}
