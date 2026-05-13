package com.example.onlineStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imagePath;          // /uploads/products/image123.jpg

    private String altText;            // альтернативный текст для SEO

    @Column(name = "is_main")
    private Boolean isMain = false;    // главное изображение товара

    @Column(name = "sort_order")
    private Integer sortOrder = 0;     // порядок сортировки

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}