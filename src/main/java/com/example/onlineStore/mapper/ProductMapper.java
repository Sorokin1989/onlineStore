package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Product;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    // Entity → DTO
    public ProductDto toDto(Product entity) {
        if (entity == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setOldPrice(entity.getOldPrice());
        dto.setImagePath(entity.getImagePath());
        dto.setInStock(entity.getInStock());
        dto.setQuantity(entity.getQuantity());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Дополнительные изображения (JSON строку → List)
        if (entity.getAdditionalImages() != null && !entity.getAdditionalImages().isEmpty()) {
            try {
                List<String> images = Arrays.asList(entity.getAdditionalImages().split(","));
                dto.setAdditionalImages(images);
            } catch (Exception e) {
                dto.setAdditionalImages(Collections.emptyList());
            }
        }

        // Категория
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }

        return dto;
    }

    // List<Entity> → List<DTO>
    public List<ProductDto> toDtoList(List<Product> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // DTO → Entity (для создания)
    public Product toEntity(ProductDto dto, Category category) {
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setOldPrice(dto.getOldPrice());
        entity.setImagePath(dto.getImagePath());
        entity.setInStock(dto.getInStock() != null ? dto.getInStock() : true);
        entity.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
        entity.setCategory(category);

        // Дополнительные изображения (List → JSON строку)
        if (dto.getAdditionalImages() != null && !dto.getAdditionalImages().isEmpty()) {
            entity.setAdditionalImages(String.join(",", dto.getAdditionalImages()));
        }

        return entity;
    }

    // DTO → Entity (для обновления)
    public void updateEntity(ProductDto dto, Product entity, Category category) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getSlug() != null) entity.setSlug(dto.getSlug());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
        if (dto.getOldPrice() != null) entity.setOldPrice(dto.getOldPrice());
        if (dto.getImagePath() != null) entity.setImagePath(dto.getImagePath());
        if (dto.getInStock() != null) entity.setInStock(dto.getInStock());
        if (dto.getQuantity() != null) entity.setQuantity(dto.getQuantity());
        if (category != null) entity.setCategory(category);

        if (dto.getAdditionalImages() != null && !dto.getAdditionalImages().isEmpty()) {
            entity.setAdditionalImages(String.join(",", dto.getAdditionalImages()));
        }
    }
}