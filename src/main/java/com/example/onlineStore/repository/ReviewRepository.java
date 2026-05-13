package com.example.onlineStore.repository;

import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.Review;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductOrderByCreatedAtDesc(Product product);

    List<Review> findByUserOrderByCreatedAtDesc(User user);

    List<Review> findByStatusOrderByCreatedAtDesc(ReviewStatus status);

    List<Review> findByProductAndStatusOrderByCreatedAtDesc(Product product, ReviewStatus status);

    boolean existsByUserAndProduct(User user, Product product);

    long countByProductAndStatus(Product product, ReviewStatus status);

    void deleteByProduct(Product product);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId AND r.status = :status")
    Double getAverageRatingByProduct(@Param("productId") Long productId, @Param("status") ReviewStatus status);
}