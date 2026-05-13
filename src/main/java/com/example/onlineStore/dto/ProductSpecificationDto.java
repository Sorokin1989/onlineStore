package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationDto {

    private Long id;
    private String specificationName;
    private String specificationUnit;
    private String value;       // "1.5"
}