package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private String productImage;
    private BigDecimal productPrice;
    private Integer quantity;
    private Integer maxAvailable;  // максимальное количество на складе

    public BigDecimal getTotalPrice() {
        if (productPrice == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public String getFormattedTotalPrice() {
        return String.format("%,.2f ₽", getTotalPrice()).replace(",", " ");
    }
}