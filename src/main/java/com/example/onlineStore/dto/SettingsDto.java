package com.example.onlineStore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDto {
    private String siteName;
    private String siteEmail;
    private String sitePhone;
    private String siteAddress;
    private String workingHours;
    private String vkUrl;
    private String telegramUrl;
    private String whatsappUrl;
    private String metaDescription;
    private String metaKeywords;
}