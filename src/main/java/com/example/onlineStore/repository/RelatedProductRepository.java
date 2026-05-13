package com.example.onlineStore.repository;

import com.example.onlineStore.entity.RelatedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RelatedProductRepository extends JpaRepository<RelatedProduct, Long> {

    List<RelatedProduct> findByProductIdOrderBySortOrderAsc(Long productId);

    void deleteByProductId(Long productId);
}