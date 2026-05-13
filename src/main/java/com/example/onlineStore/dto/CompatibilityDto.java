package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompatibilityDto {

    private Long id;
    private String name;        // "iPhone 15"
    private String brand;       // "Apple"
    private Boolean active;
}