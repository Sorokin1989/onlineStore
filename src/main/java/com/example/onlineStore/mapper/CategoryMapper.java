package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.CategoryDto;
import com.example.onlineStore.entity.Category;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());           // ← если есть slug
        dto.setDescription(category.getDescription());
        dto.setImagePath(category.getImagePath());
        dto.setSortOrder(category.getSortOrder());
        dto.setActive(category.getActive());

        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }

        return dto;
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        if (categories == null) return Collections.emptyList();
        return categories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto toDtoWithChildren(Category category) {
        CategoryDto dto = toDto(category);
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            dto.setChildren(toDtoList(category.getChildren()));
        }
        return dto;
    }

    // Для создания (без ID)
    public Category toEntity(CategoryDto dto, Category parent) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setDescription(dto.getDescription());
        category.setImagePath(dto.getImagePath());
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        category.setActive(dto.getActive() != null ? dto.getActive() : true);
        category.setParent(parent);
        return category;
    }

    // Для обновления (существующая сущность)
    public void updateEntity(CategoryDto dto, Category entity, Category parent) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getSlug() != null) entity.setSlug(dto.getSlug());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getImagePath() != null) entity.setImagePath(dto.getImagePath());
        if (dto.getSortOrder() != null) entity.setSortOrder(dto.getSortOrder());
        if (dto.getActive() != null) entity.setActive(dto.getActive());
        if (parent != null) entity.setParent(parent);
    }
}