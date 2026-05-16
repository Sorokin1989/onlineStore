package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {

    private Long id;
    private String slug;
    private String title;
    private String content;
    private String metaTitle;
    private String metaDescription;
    private Integer sortOrder;
    private Boolean active;

    // Вспомогательные методы
    public String getShortContent() {
        if (content == null) return "";
        if (content.length() <= 150) return content;
        return content.substring(0, 150) + "...";
    }

    public String getStatusDisplay() {
        return active != null && active ? "Активна" : "Неактивна";
    }
}