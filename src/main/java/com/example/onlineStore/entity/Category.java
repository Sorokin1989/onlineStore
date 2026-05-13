package com.example.onlineStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String slug;  // для ЧПУ: /catalog/smartfony

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();
}