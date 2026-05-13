package com.example.onlineStore.service;

import com.example.onlineStore.entity.ProductSpecification;
import com.example.onlineStore.repository.ProductSpecificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSpecificationService {

    private final ProductSpecificationRepository productSpecificationRepository;

    public List<ProductSpecification> getSpecificationsByProductId(Long productId) {
        return productSpecificationRepository.findByProductId(productId);
    }

    @Transactional
    public ProductSpecification createSpecification(ProductSpecification specification) {
        return productSpecificationRepository.save(specification);
    }

    @Transactional
    public void deleteSpecification(Long id) {
        productSpecificationRepository.deleteById(id);
    }

    @Transactional
    public void deleteByProductId(Long productId) {
        productSpecificationRepository.deleteByProductId(productId);
    }
}