package com.example.onlineStore.service;

import com.example.onlineStore.dto.ReviewDto;
import com.example.onlineStore.dto.ReviewRequest;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.Review;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.ReviewStatus;
import com.example.onlineStore.mapper.ReviewMapper;
import com.example.onlineStore.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    // Получить все отзывы
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Получить одобренные отзывы
    public List<Review> getApprovedReviews() {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(ReviewStatus.APPROVED);
    }

    // Получить отзывы по товару
    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    // Получить одобренные отзывы по товару
    public List<Review> getApprovedReviewsByProduct(Product product) {
        return reviewRepository.findByProductAndStatusOrderByCreatedAtDesc(product, ReviewStatus.APPROVED);
    }

    // Получить отзывы пользователя
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // Получить отзывы со статусом PENDING (на модерации)
    public List<Review> getPendingReviews() {
        return reviewRepository.findByStatusOrderByCreatedAtDesc(ReviewStatus.PENDING);
    }

    // Создать новый отзыв
    @Transactional
    public Review createReview(Product product, User user, Integer rating, String comment) {
        // Проверка, не оставлял ли пользователь уже отзыв на этот товар
        if (reviewRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("Вы уже оставили отзыв на этот товар");
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);
        review.setStatus(ReviewStatus.PENDING);
        review.setCreatedAt(java.time.LocalDateTime.now());

        return reviewRepository.save(review);
    }

    // Создать отзыв из Request DTO
    @Transactional
    public Review createReview(ReviewRequest request, User user, Product product) {
        return createReview(product, user, request.getRating(), request.getComment());
    }

    // Обновить статус отзыва (модерация)
    @Transactional
    public Review updateReviewStatus(Long reviewId, ReviewStatus status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));
        review.setStatus(status);
        return reviewRepository.save(review);
    }

    // Одобрить отзыв
    @Transactional
    public Review approveReview(Long reviewId) {
        return updateReviewStatus(reviewId, ReviewStatus.APPROVED);
    }

    // Отклонить отзыв
    @Transactional
    public Review rejectReview(Long reviewId) {
        return updateReviewStatus(reviewId, ReviewStatus.REJECTED);
    }

    // Удалить отзыв
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // Удалить все отзывы товара
    @Transactional
    public void deleteReviewsByProduct(Product product) {
        reviewRepository.deleteByProduct(product);
    }

    // Получить средний рейтинг товара
    public Double getAverageRatingByProduct(Product product) {
        Double avg = reviewRepository.getAverageRatingByProduct(product.getId(), ReviewStatus.APPROVED);
        return avg != null ? avg : 0.0;
    }

    // Получить количество отзывов товара
    public Long getReviewsCountByProduct(Product product) {
        return reviewRepository.countByProductAndStatus(product, ReviewStatus.APPROVED);
    }

    // Проверить, оставлял ли пользователь отзыв на товар
    public boolean hasUserReviewedProduct(User user, Product product) {
        return reviewRepository.existsByUserAndProduct(user, product);
    }
}