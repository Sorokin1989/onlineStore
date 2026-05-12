package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDto {

    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal total;

    public String getFormattedPrice() {
        return String.format("%,.2f ₽", price).replace(",", " ");
    }

    public String getFormattedTotal() {
        return String.format("%,.2f ₽", total).replace(",", " ");
    }
}