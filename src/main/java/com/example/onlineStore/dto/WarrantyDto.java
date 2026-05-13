package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyDto {

    private Long id;
    private String name;
    private Integer durationMonths;
    private String description;
    private BigDecimal additionalPrice;
    private Boolean active;
}