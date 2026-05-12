package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductFilterDto {

    private String query;           // поисковый запрос
    private Long categoryId;        // фильтр по категории
    private BigDecimal minPrice;    // минимальная цена
    private BigDecimal maxPrice;    // максимальная цена
    private Boolean inStock;        // только в наличии

    private String sortBy;          // price_asc, price_desc, name_asc, name_desc, newest
    private Integer page = 0;       // страница (0-индексация)
    private Integer size = 12;      // товаров на страницу

    // Вспомогательные методы
    public boolean hasQuery() {
        return query != null && !query.trim().isEmpty();
    }

    public boolean hasCategory() {
        return categoryId != null && categoryId > 0;
    }

    public boolean hasPriceRange() {
        return (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) > 0)
                || (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) > 0);
    }

    public String getSortField() {
        if (sortBy == null) return "createdAt";
        switch (sortBy) {
            case "price_asc": return "price";
            case "price_desc": return "price";
            case "name_asc": return "name";
            case "name_desc": return "name";
            case "newest": return "createdAt";
            default: return "createdAt";
        }
    }

    public String getSortDirection() {
        if (sortBy == null) return "DESC";
        switch (sortBy) {
            case "price_asc": return "ASC";
            case "price_desc": return "DESC";
            case "name_asc": return "ASC";
            case "name_desc": return "DESC";
            case "newest": return "DESC";
            default: return "DESC";
        }
    }
}