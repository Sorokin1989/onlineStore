package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorDto {

    private Long id;
    private String name;        // "Чёрный"
    private String hex;         // "#000000"
    private Boolean active;
}