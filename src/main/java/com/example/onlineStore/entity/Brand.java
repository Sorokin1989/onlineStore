package com.example.onlineStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    private String logo;

    private String description;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "brand")
    private List<Product> products = new ArrayList<>();
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}