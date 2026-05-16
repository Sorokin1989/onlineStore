package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String imagePath;
    private MultipartFile mainImageFile;
    private Long categoryId;
    private String categoryName;
    private String categorySlug;
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private Long warrantyId;
    private String warrantyName;
    private Integer warrantyMonths;
    private Integer quantity;
    private Boolean inStock = true;
    private Boolean active = true;
    private Boolean featured = false;
    private String metaTitle;
    private String metaDescription;
    private String seoKeywords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Список изображений
    private List<String> images;
    private String mainImage;

    // Связанные данные
    private List<CompatibilityDto> compatibilities;
    private List<ColorDto> colors;
    private List<TagDto> tags;
    private List<ProductSpecificationDto> specifications;

    // Форматированная цена (добавить этот метод)
    public String getFormattedPrice() {
        if (price == null) return "0 ₽";
        return String.format("%,.2f ₽", price).replace(",", " ");
    }

    // Форматированная старая цена
    public String getFormattedOldPrice() {
        if (oldPrice == null) return null;
        return String.format("%,.2f ₽", oldPrice).replace(",", " ");
    }
}