package com.example.onlineStore.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductFilterRequest {

    private String query;           // поисковый запрос
    private Long categoryId;        // фильтр по категории
    private List<Long> brandIds;    // фильтр по брендам (множественный)
    private List<Long> colorIds;    // фильтр по цветам
    private List<Long> compatibilityIds; // фильтр по совместимости
    private BigDecimal minPrice;    // минимальная цена
    private BigDecimal maxPrice;    // максимальная цена
    private Boolean inStock;        // только в наличии

    // Фильтр по характеристикам (specification_id -> value)
    private List<SpecificationFilter> specifications;

    private String sortBy;          // price_asc, price_desc, name_asc, name_desc, newest
    private Integer page = 0;
    private Integer size = 12;

    @Data
    public static class SpecificationFilter {
        private Long specificationId;
        private String value;
    }
}