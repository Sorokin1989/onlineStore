package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT p FROM Product p WHERE " +
            "(:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%'))) AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:inStock IS NULL OR (p.inStock = true AND p.quantity > 0))")
    Page<Product> searchProducts(@Param("query") String query,
                                 @Param("category") Category category,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice,
                                 @Param("inStock") Boolean inStock,
                                 Pageable pageable);
}
