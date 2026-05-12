package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String role;           // ROLE_USER, ROLE_ADMIN
    private LocalDateTime createdAt;

    // Для форм (пароль не храним в DTO для чтения)
    private String password;       // только для создания/обновления

    // Статистика (для админки)
    private Long ordersCount;
    private BigDecimal totalSpent;

    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(role);
    }

    public String getInitials() {
        if (fullName == null || fullName.isEmpty()) return email;
        String[] parts = fullName.split(" ");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1].charAt(0) + ".";
        }
        return fullName;
    }
}