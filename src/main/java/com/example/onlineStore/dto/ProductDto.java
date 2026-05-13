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
    private Boolean inStock;
    private Integer quantity;

    // Категория
    private Long categoryId;
    private String categoryName;
    private String categorySlug;

    // Бренд
    private Long brandId;
    private String brandName;
    private String brandLogo;

    // Гарантия
    private Long warrantyId;
    private String warrantyName;
    private Integer warrantyMonths;

    // Совместимость
    private List<CompatibilityDto> compatibilities = new ArrayList<>();

    // Цвета
    private List<ColorDto> colors = new ArrayList<>();

    // Теги
    private List<TagDto> tags = new ArrayList<>();

    // Характеристики
    private List<ProductSpecificationDto> specifications = new ArrayList<>();

    // Изображения
    private List<String> images = new ArrayList<>();
    private String mainImage;
    private String imagePath;

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
}