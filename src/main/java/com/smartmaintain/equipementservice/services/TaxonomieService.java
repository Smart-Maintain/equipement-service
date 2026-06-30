package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.entities.Taxonomie;
import com.smartmaintain.equipementservice.repositories.TaxonomieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaxonomieService {

    private final TaxonomieRepository taxonomieRepository;

    public TaxonomieService(TaxonomieRepository taxonomieRepository) {
        this.taxonomieRepository = taxonomieRepository;
    }

    public List<Taxonomie> getAllTaxonomies() {
        return taxonomieRepository.findAll();
    }

    public Optional<Taxonomie> getTaxonomieById(Long id) {
        return taxonomieRepository.findById(id);
    }

    public Taxonomie saveTaxonomie(Taxonomie taxonomie) {
        return taxonomieRepository.save(taxonomie);
    }

    public Taxonomie updateTaxonomie(Long id, Taxonomie taxonomieDetails) {
        Taxonomie taxonomie = taxonomieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Taxonomie not found with id: " + id));

        taxonomie.setCode(taxonomieDetails.getCode());
        taxonomie.setNom(taxonomieDetails.getNom());
        taxonomie.setDescription(taxonomieDetails.getDescription());

        return taxonomieRepository.save(taxonomie);
    }

    public void deleteTaxonomie(Long id) {
        taxonomieRepository.deleteById(id);
    }
}