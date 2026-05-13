package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private Integer rating;
    private String comment;
    private String status;           // PENDING, APPROVED, REJECTED
    private String statusDisplay;
    private LocalDateTime createdAt;

    // Данные пользователя
    private Long userId;
    private String userName;
    private String userEmail;

    // Данные товара
    private Long productId;
    private String productName;
    private String productSlug;

    // Вспомогательные методы
    public String getRatingStars() {
        if (rating == null) return "";
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) stars.append("★");
        for (int i = rating; i < 5; i++) stars.append("☆");
        return stars.toString();
    }

    public String getStatusDisplay() {
        if (statusDisplay != null) return statusDisplay;
        switch (status) {
            case "PENDING": return "На модерации";
            case "APPROVED": return "Одобрен";
            case "REJECTED": return "Отклонён";
            default: return status;
        }
    }

    public boolean isApproved() {
        return "APPROVED".equals(status);
    }
}