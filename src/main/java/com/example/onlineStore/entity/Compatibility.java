package com.example.onlineStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compatibilities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compatibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // "iPhone 15", "Samsung Galaxy S24"

    private String brand; // "Apple", "Samsung"

    @Column(name = "is_active")
    private Boolean active = true;

    @ManyToMany(mappedBy = "compatibilities")
    private List<Product> products = new ArrayList<>();
}