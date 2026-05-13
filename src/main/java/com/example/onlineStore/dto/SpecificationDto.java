package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationDto {

    private Long id;
    private String name;        // "Длина кабеля"
    private String unit;        // "см"
    private Boolean filterable;
    private Boolean active;
}