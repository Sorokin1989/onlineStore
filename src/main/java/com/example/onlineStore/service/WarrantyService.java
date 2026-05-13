package com.example.onlineStore.service;

import com.example.onlineStore.entity.Warranty;
import com.example.onlineStore.repository.WarrantyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarrantyService {

    private final WarrantyRepository warrantyRepository;

    public List<Warranty> getAllWarranties() {
        return warrantyRepository.findAll();
    }

    public List<Warranty> getActiveWarranties() {
        return warrantyRepository.findByActiveTrueOrderByNameAsc();
    }

    public Optional<Warranty> getWarrantyById(Long id) {
        return warrantyRepository.findById(id);
    }

    @Transactional
    public Warranty createWarranty(Warranty warranty) {
        return warrantyRepository.save(warranty);
    }

    @Transactional
    public Warranty updateWarranty(Long id, Warranty updated) {
        Warranty existing = warrantyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Гарантия не найдена"));

        existing.setName(updated.getName());
        existing.setDurationMonths(updated.getDurationMonths());
        existing.setDescription(updated.getDescription());
        existing.setAdditionalPrice(updated.getAdditionalPrice());
        existing.setActive(updated.getActive());

        return warrantyRepository.save(existing);
    }

    @Transactional
    public void deleteWarranty(Long id) {
        warrantyRepository.deleteById(id);
    }
}