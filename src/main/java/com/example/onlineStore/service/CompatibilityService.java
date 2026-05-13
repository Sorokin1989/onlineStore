package com.example.onlineStore.service;

import com.example.onlineStore.entity.Compatibility;
import com.example.onlineStore.repository.CompatibilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompatibilityService {

    private final CompatibilityRepository compatibilityRepository;

    public List<Compatibility> getAllCompatibilities() {
        return compatibilityRepository.findAll();
    }

    public List<Compatibility> getActiveCompatibilities() {
        return compatibilityRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<Compatibility> getCompatibilitiesByBrand(String brand) {
        return compatibilityRepository.findByBrand(brand);
    }

    public Optional<Compatibility> getCompatibilityById(Long id) {
        return compatibilityRepository.findById(id);
    }

    public Optional<Compatibility> getCompatibilityByName(String name) {
        return compatibilityRepository.findByName(name);
    }

    @Transactional
    public Compatibility createCompatibility(Compatibility compatibility) {
        return compatibilityRepository.save(compatibility);
    }

    @Transactional
    public Compatibility updateCompatibility(Long id, Compatibility updated) {
        Compatibility existing = compatibilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Совместимость не найдена"));

        existing.setName(updated.getName());
        existing.setBrand(updated.getBrand());
        existing.setActive(updated.getActive());

        return compatibilityRepository.save(existing);
    }

    @Transactional
    public void deleteCompatibility(Long id) {
        compatibilityRepository.deleteById(id);
    }
}