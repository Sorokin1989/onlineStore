package com.example.onlineStore.service;

import com.example.onlineStore.entity.Specification;
import com.example.onlineStore.repository.SpecificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecificationService {

    private final SpecificationRepository specificationRepository;

    public List<Specification> getAllSpecifications() {
        return specificationRepository.findAll();
    }

    public List<Specification> getActiveSpecifications() {
        return specificationRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<Specification> getFilterableSpecifications() {
        return specificationRepository.findByFilterableTrue();
    }

    public Optional<Specification> getSpecificationById(Long id) {
        return specificationRepository.findById(id);
    }

    public Optional<Specification> getSpecificationByName(String name) {
        return specificationRepository.findByName(name);
    }

    @Transactional
    public Specification createSpecification(Specification specification) {
        return specificationRepository.save(specification);
    }

    @Transactional
    public Specification updateSpecification(Long id, Specification updated) {
        Specification existing = specificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Характеристика не найдена"));

        existing.setName(updated.getName());
        existing.setUnit(updated.getUnit());
        existing.setFilterable(updated.getFilterable());
        existing.setActive(updated.getActive());

        return specificationRepository.save(existing);
    }

    @Transactional
    public void deleteSpecification(Long id) {
        specificationRepository.deleteById(id);
    }
}