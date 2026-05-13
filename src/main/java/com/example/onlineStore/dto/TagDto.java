package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {

    private Long id;
    private String name;        // "хит", "новинка"
    private String slug;
    private Boolean active;
}