package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterDto {

    private String query;                    // поисковый запрос
    private Long categoryId;                 // фильтр по категории
    private List<Long> brandIds;             // фильтр по брендам
    private List<Long> compatibilityIds;     // фильтр по совместимости
    private List<Long> colorIds;             // фильтр по цветам
    private List<Long> tagIds;               // фильтр по тегам
    private List<Long> specificationIds;     // фильтр по характеристикам
    private BigDecimal minPrice;             // минимальная цена
    private BigDecimal maxPrice;             // максимальная цена
    private Boolean inStock;                 // только в наличии
    private Boolean onSale;                  // только со скидкой
    private Boolean isNew;                   // только новинки
    private Boolean isFeatured;              // только рекомендуемые

    private String sortBy;                   // price_asc, price_desc, name_asc, name_desc, newest, rating
    private Integer page = 0;                // страница (0-индексация)
    private Integer size = 12;               // товаров на страницу

    // Вспомогательные методы
    public boolean hasQuery() {
        return query != null && !query.trim().isEmpty();
    }

    public boolean hasCategory() {
        return categoryId != null && categoryId > 0;
    }

    public boolean hasBrands() {
        return brandIds != null && !brandIds.isEmpty();
    }

    public boolean hasCompatibilities() {
        return compatibilityIds != null && !compatibilityIds.isEmpty();
    }

    public boolean hasColors() {
        return colorIds != null && !colorIds.isEmpty();
    }

    public boolean hasTags() {
        return tagIds != null && !tagIds.isEmpty();
    }

    public boolean hasSpecifications() {
        return specificationIds != null && !specificationIds.isEmpty();
    }

    public boolean hasPriceRange() {
        return (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) > 0)
                || (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0);
    }

    public boolean hasOnSale() {
        return onSale != null && onSale;
    }

    public boolean hasNew() {
        return isNew != null && isNew;
    }

    public boolean hasFeatured() {
        return isFeatured != null && isFeatured;
    }

    public String getSortField() {
        if (sortBy == null) return "createdAt";
        switch (sortBy) {
            case "price_asc":
            case "price_desc":
                return "price";
            case "name_asc":
            case "name_desc":
                return "name";
            case "rating":
                return "averageRating";
            case "newest":
                return "createdAt";
            default:
                return "createdAt";
        }
    }

    public String getSortDirection() {
        if (sortBy == null) return "DESC";
        switch (sortBy) {
            case "price_asc":
            case "name_asc":
                return "ASC";
            case "price_desc":
            case "name_desc":
            case "newest":
            case "rating":
                return "DESC";
            default:
                return "DESC";
        }
    }
}