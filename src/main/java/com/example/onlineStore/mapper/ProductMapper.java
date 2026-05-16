package com.example.onlineStore.mapper;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Brand;
import com.example.onlineStore.entity.Category;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.Warranty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final BrandMapper brandMapper;
    private final CompatibilityMapper compatibilityMapper;
    private final ColorMapper colorMapper;
    private final TagMapper tagMapper;
    private final ProductSpecificationMapper productSpecificationMapper;
    private final WarrantyMapper warrantyMapper;

    public ProductDto toDto(Product entity) {
        if (entity == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setOldPrice(entity.getOldPrice());
        dto.setInStock(entity.getInStock());
        dto.setQuantity(entity.getQuantity());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Категория
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
            dto.setCategorySlug(entity.getCategory().getSlug());
        }

        // Бренд
        if (entity.getBrand() != null) {
            dto.setBrandId(entity.getBrand().getId());
            dto.setBrandName(entity.getBrand().getName());
            dto.setBrandLogo(entity.getBrand().getLogo());
        }

        // Гарантия
        if (entity.getWarranty() != null) {
            dto.setWarrantyId(entity.getWarranty().getId());
            dto.setWarrantyName(entity.getWarranty().getName());
            dto.setWarrantyMonths(entity.getWarranty().getDurationMonths());
        }

        // Совместимость
        if (entity.getCompatibilities() != null) {
            dto.setCompatibilities(compatibilityMapper.toDtoList(entity.getCompatibilities()));
        }

        // Цвета
        if (entity.getColors() != null) {
            dto.setColors(colorMapper.toDtoList(entity.getColors()));
        }

        // Теги
        if (entity.getTags() != null) {
            dto.setTags(tagMapper.toDtoList(entity.getTags()));
        }

        // Характеристики
        if (entity.getSpecifications() != null) {
            dto.setSpecifications(productSpecificationMapper.toDtoList(entity.getSpecifications()));
        }

        // Изображения
        if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            dto.setImages(entity.getAllImagePaths());
            dto.setMainImage(entity.getMainImage());
        }

        return dto;
    }

    public List<ProductDto> toDtoList(List<Product> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ========== МЕТОДЫ toEntity ==========

    // DTO → Entity (для создания нового товара)
    public Product toEntity(ProductDto dto, Category category, Brand brand) {
        if (dto == null) return null;

        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setOldPrice(dto.getOldPrice());
        entity.setInStock(dto.getInStock() != null ? dto.getInStock() : true);
        entity.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
        entity.setCategory(category);
        entity.setBrand(brand);

        // Гарантия
//        if (dto.getWarrantyId() != null) {
//            Warranty warranty = new Warranty();
//            warranty.setId(dto.getWarrantyId());
//            entity.setWarranty(warranty);
//        }

        return entity;
    }

    // DTO → Entity (для обновления существующего товара)
    public void updateEntity(ProductDto dto, Product entity, Category category, Brand brand) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getSlug() != null) entity.setSlug(dto.getSlug());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
        if (dto.getOldPrice() != null) entity.setOldPrice(dto.getOldPrice());
        if (dto.getInStock() != null) entity.setInStock(dto.getInStock());
        if (dto.getQuantity() != null) entity.setQuantity(dto.getQuantity());
        if (category != null) entity.setCategory(category);
        if (brand != null) entity.setBrand(brand);

        if (dto.getWarrantyId() != null) {
            Warranty warranty = new Warranty();
            warranty.setId(dto.getWarrantyId());
            entity.setWarranty(warranty);
        }

        if (dto.getImagePath() != null) entity.setImagePath(dto.getImagePath());
    }

    // Простой метод toEntity (без связей)
    public Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        Product entity = new Product();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setOldPrice(dto.getOldPrice());
        entity.setImagePath(dto.getImagePath());
        entity.setInStock(dto.getInStock() != null ? dto.getInStock() : true);
        entity.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);

        return entity;
    }
}