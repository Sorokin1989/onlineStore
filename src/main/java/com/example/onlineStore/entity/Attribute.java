package com.example.onlineStore.entity;

import com.example.onlineStore.enums.AttributeType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // "Бренд", "Материал", "Размер"

    private String unit;  // "см", "кг", "шт"

    @Enumerated(EnumType.STRING)
    private AttributeType type;  // TEXT, NUMBER, COLOR


}