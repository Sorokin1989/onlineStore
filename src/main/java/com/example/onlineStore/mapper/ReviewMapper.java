package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.ReviewDto;
import com.example.onlineStore.dto.ReviewRequest;
import com.example.onlineStore.entity.Review;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.entity.Product;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review entity) {
        if (entity == null) return null;

        ReviewDto dto = new ReviewDto();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : "PENDING");
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getFullName() != null
                    ? entity.getUser().getFullName()
                    : entity.getUser().getEmail());
            dto.setUserEmail(entity.getUser().getEmail());
        }

        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
            dto.setProductName(entity.getProduct().getName());
            dto.setProductSlug(entity.getProduct().getSlug());
        }

        return dto;
    }

    public List<ReviewDto> toDtoList(List<Review> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Из Request в Entity
    public Review toEntity(ReviewRequest request, User user, Product product) {
        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUser(user);
        review.setProduct(product);
        review.setStatus(com.example.onlineStore.enums.ReviewStatus.PENDING);
        review.setCreatedAt(java.time.LocalDateTime.now());
        return review;
    }
}