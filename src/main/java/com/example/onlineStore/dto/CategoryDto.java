package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private String slug;           // ← добавить
    private String description;
    private String imagePath;
    private Integer sortOrder;
    private Boolean active;

    // Для иерархии категорий
    private Long parentId;
    private String parentName;
    private List<CategoryDto> children = new ArrayList<>();

    // Статистика (для админки)
    private Long productsCount;

    // Конструктор для базовой информации
    public CategoryDto(Long id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
    }

    // Вспомогательные методы
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
}