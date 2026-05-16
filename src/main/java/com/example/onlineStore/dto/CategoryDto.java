package com.example.onlineStore.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String imagePath;
    private MultipartFile imageFile;  // для загрузки файла
    private Long parentId;
    private String parentName;
    private Integer sortOrder = 0;
    private Boolean active = true;

    // Добавьте эти поля:
    private String seoTitle;      // SEO заголовок
    private String seoDescription; // SEO описание

    // Если нужны другие поля:
    private String metaTitle;
    private String metaDescription;
    private String seoKeywords;
}