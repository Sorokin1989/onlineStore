package com.example.onlineStore.service;

import com.example.onlineStore.entity.RelatedProduct;
import com.example.onlineStore.repository.RelatedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatedProductService {

    private final RelatedProductRepository relatedProductRepository;

    public List<RelatedProduct> getRelatedProductsByProductId(Long productId) {
        return relatedProductRepository.findByProductIdOrderBySortOrderAsc(productId);
    }

    @Transactional
    public RelatedProduct createRelatedProduct(RelatedProduct relatedProduct) {
        return relatedProductRepository.save(relatedProduct);
    }

    @Transactional
    public void deleteRelatedProduct(Long id) {
        relatedProductRepository.deleteById(id);
    }

    @Transactional
    public void deleteByProductId(Long productId) {
        relatedProductRepository.deleteByProductId(productId);
    }
}