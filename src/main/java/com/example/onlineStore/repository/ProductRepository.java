package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndActiveTrueOrderByCreatedAtDesc(Category category);

    Optional<Product> findBySlug(String slug);

    Slice<Product> findAllByActiveTrue(Pageable pageable);

    List<Product> findByOldPriceIsNotNullAndActiveTrue(Pageable pageable);

    Page<Product> searchProducts(String query, Category category, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock, Pageable pageable);
}
