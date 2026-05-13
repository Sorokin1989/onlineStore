package com.example.onlineStore.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "specifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // "Длина кабеля", "Материал", "Тип стекла"

    private String unit;  // "см", "м", "мм", "г"

    @Column(name = "is_filterable")
    private Boolean filterable = true;  // можно фильтровать по этой характеристике

    @Column(name = "is_active")
    private Boolean active = true;
}