package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {

    private Long id;
    private String name;
    private String slug;
    private String logo;
    private String description;
    private Boolean active;
    private Long productsCount;  // количество товаров бренда
}