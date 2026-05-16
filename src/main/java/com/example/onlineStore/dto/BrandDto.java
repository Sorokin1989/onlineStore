package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String logo;           // путь к логотипу в БД
    private MultipartFile logoFile; // ← ДОБАВИТЬ ЭТО ПОЛЕ для загрузки файла
    private Integer sortOrder = 0;
    private Boolean active = true;
}