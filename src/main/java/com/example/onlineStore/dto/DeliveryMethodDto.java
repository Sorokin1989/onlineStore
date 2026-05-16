package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMethodDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String estimatedDays;
    private Boolean active;
}