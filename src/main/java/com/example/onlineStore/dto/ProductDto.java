package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String imagePath;
    private List<String> additionalImages = new ArrayList<>();
    private Boolean inStock;
    private Integer quantity;

    // Категория
    private Long categoryId;
    private String categoryName;
    private String categorySlug;

    // Метаданные
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Процент скидки
    public Integer getDiscountPercent() {
        if (oldPrice != null && oldPrice.compareTo(BigDecimal.ZERO) > 0) {
            return oldPrice.subtract(price)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(oldPrice, 0, java.math.RoundingMode.HALF_UP)
                    .intValue();
        }
        return 0;
    }

    public boolean isOnSale() {
        return oldPrice != null && oldPrice.compareTo(price) > 0;
    }

    public String getUrl() {
        return "/product/" + slug;
    }

    public String getFormattedPrice() {
        return String.format("%,.2f ₽", price).replace(",", " ");
    }
}