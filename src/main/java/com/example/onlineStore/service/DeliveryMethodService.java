package com.example.onlineStore.service;

import com.example.onlineStore.entity.DeliveryMethod;
import com.example.onlineStore.repository.DeliveryMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryMethodService {

    private final DeliveryMethodRepository deliveryMethodRepository;

    public List<DeliveryMethod> getAllDeliveryMethods() {
        return deliveryMethodRepository.findAll();
    }

    public List<DeliveryMethod> getActiveDeliveryMethods() {
        return deliveryMethodRepository.findByActiveTrue();
    }

    public Optional<DeliveryMethod> getDeliveryMethodById(Long id) {
        return deliveryMethodRepository.findById(id);
    }

    @Transactional
    public DeliveryMethod createDeliveryMethod(DeliveryMethod deliveryMethod) {
        return deliveryMethodRepository.save(deliveryMethod);
    }

    @Transactional
    public DeliveryMethod updateDeliveryMethod(Long id, DeliveryMethod updated) {
        DeliveryMethod existing = deliveryMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Способ доставки не найден"));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setEstimatedDays(updated.getEstimatedDays());
        existing.setActive(updated.getActive());
        return deliveryMethodRepository.save(existing);
    }

    @Transactional
    public void deleteDeliveryMethod(Long id) {
        deliveryMethodRepository.deleteById(id);
    }
}