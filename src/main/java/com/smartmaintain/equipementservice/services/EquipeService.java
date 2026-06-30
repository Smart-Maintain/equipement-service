package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.EquipeRequest;
import com.smartmaintain.equipementservice.entities.Equipe;
import com.smartmaintain.equipementservice.repositories.EquipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class EquipeService {
    private final EquipeRepository repository;

    public EquipeService(EquipeRepository repository) { this.repository = repository; }

    public List<Equipe> getAll() { return repository.findAll(); }
    public Equipe getById(UUID id) { return repository.findById(id).orElseThrow(); }
    public Equipe save(Equipe e) { return repository.save(e); }
    public void delete(UUID id) { repository.deleteById(id); }

    public Equipe createTeam(EquipeRequest request) {
        Equipe equipe = new Equipe();
        applyRequest(equipe, request);
        return repository.save(equipe);
    }

    public Equipe updateTeam(UUID id, EquipeRequest request) {
        Equipe equipe = getById(id);
        applyRequest(equipe, request);
        return repository.save(equipe);
    }

    private void applyRequest(Equipe equipe, EquipeRequest request) {
        if (request.leaderEngineerId() == null) {
            throw new IllegalArgumentException("A team must have one leader engineer.");
        }
        if (request.technicianIds() == null || request.technicianIds().isEmpty()) {
            throw new IllegalArgumentException("A team must have at least one technician.");
        }

        equipe.setNom(request.nom());
        equipe.setSpecialite(request.specialite());
        equipe.setLeaderEngineerId(request.leaderEngineerId());
        equipe.setLeaderEngineerName(request.leaderEngineerName());
        equipe.setTechnicianIds(request.technicianIds());
        equipe.setTechnicianNames(request.technicianNames());
    }
}
