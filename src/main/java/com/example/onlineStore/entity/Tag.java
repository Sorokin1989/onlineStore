package com.example.onlineStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // "хит", "новинка", "скидка"

    @Column(unique = true)
    private String slug;

    @Column(name = "is_active")
    private Boolean active = true;

    @ManyToMany(mappedBy = "tags")
    private List<Product> products = new ArrayList<>();
}