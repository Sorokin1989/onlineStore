package com.example.onlineStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // "Чёрный", "Белый"

    private String hex;   // "#000000", "#FFFFFF"

    @Column(name = "is_active")
    private Boolean active = true;

    @ManyToMany(mappedBy = "colors")
    private List<Product> products = new ArrayList<>();
}