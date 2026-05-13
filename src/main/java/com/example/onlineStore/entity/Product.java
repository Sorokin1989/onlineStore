package com.example.onlineStore.entity;

import com.example.onlineStore.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "old_price", precision = 10, scale = 2)
    private BigDecimal oldPrice;

    // ===== ИЗОБРАЖЕНИЯ (перенесены в отдельную сущность) =====
    // Временно оставляем для обратной совместимости, но лучше использовать ProductImage
    @Column(name = "image_path")
    private String imagePath;  // @Deprecated - используйте ProductImage

    @Column(name = "additional_images", columnDefinition = "TEXT")
    private String additionalImages;  // @Deprecated - используйте ProductImage

    // ===== НОВОЕ: связь с изображениями =====
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("isMain DESC, sortOrder ASC")
    private List<ProductImage> images = new ArrayList<>();

    @Column(name = "in_stock")
    private Boolean inStock = true;

    private Integer quantity = 0;

    // ===== СВЯЗИ =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warranty_id")
    private Warranty warranty;

    // ===== СОВМЕСТИМОСТЬ =====
    @ManyToMany
    @JoinTable(
            name = "product_compatibilities",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "compatibility_id")
    )
    private List<Compatibility> compatibilities = new ArrayList<>();

    // ===== ЦВЕТА =====
    @ManyToMany
    @JoinTable(
            name = "product_colors",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private List<Color> colors = new ArrayList<>();

    // ===== ТЕГИ =====
    @ManyToMany
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    // ===== ХАРАКТЕРИСТИКИ =====
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSpecification> specifications = new ArrayList<>();

    // ===== СВЯЗАННЫЕ ТОВАРЫ =====
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelatedProduct> relatedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "relatedProduct")
    private List<RelatedProduct> relatedFrom = new ArrayList<>();

    // ===== ОТЗЫВЫ =====
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    // ===== ИЗБРАННОЕ =====
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Favorite> favorites = new ArrayList<>();

    // ===== МЕТАДАННЫЕ =====
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "seo_keywords")
    private String seoKeywords;

    // ===== СТАТУС =====
    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "is_featured")
    private Boolean featured = false;  // рекомендуется товар

    // ===== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =====

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Получить главное изображение
    public String getMainImage() {
        if (images != null && !images.isEmpty()) {
            return images.stream()
                    .filter(ProductImage::getIsMain)
                    .findFirst()
                    .map(ProductImage::getImagePath)
                    .orElse(images.get(0).getImagePath());
        }
        return imagePath;  // fallback на старое поле
    }

    // Получить все пути изображений
    public List<String> getAllImagePaths() {
        if (images != null && !images.isEmpty()) {
            return images.stream()
                    .map(ProductImage::getImagePath)
                    .collect(Collectors.toList());
        }
        List<String> result = new ArrayList<>();
        if (imagePath != null) result.add(imagePath);
        return result;
    }

    // Получить средний рейтинг
    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .filter(r -> r.getStatus() == ReviewStatus.APPROVED)
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    // Получить количество отзывов
    public Integer getReviewsCount() {
        if (reviews == null) return 0;
        return (int) reviews.stream()
                .filter(r -> r.getStatus() == ReviewStatus.APPROVED)
                .count();
    }

    // Проверка наличия
    public boolean isAvailable() {
        return inStock != null && inStock && quantity > 0;
    }
}