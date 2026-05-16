package com.example.onlineStore.service;

import com.example.onlineStore.dto.SettingsDto;
import com.example.onlineStore.entity.SettingsEntity;
import com.example.onlineStore.repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final SettingsRepository settingsRepository;

    @PostConstruct
    public void initDefaultSettings() {
        createIfNotExists("site_name", "MobileShop", "Название сайта");
        createIfNotExists("site_email", "shop@mobileshop.ru", "Email магазина");
        createIfNotExists("site_phone", "+7 (800) 123-45-67", "Телефон магазина");
        createIfNotExists("site_address", "г. Чебоксары, ул. Примерная, д. 123", "Адрес магазина");
        createIfNotExists("working_hours", "Пн-Пт: 10:00 - 20:00, Сб-Вс: 11:00 - 18:00", "Режим работы");
        createIfNotExists("vk_url", "https://vk.com/mobileshop", "Ссылка на VK");
        createIfNotExists("telegram_url", "https://t.me/mobileshop", "Ссылка на Telegram");
        createIfNotExists("whatsapp_url", "https://wa.me/78001234567", "Ссылка на WhatsApp");
        createIfNotExists("meta_description", "Интернет-магазин мобильных аксессуаров", "Meta описание");
        createIfNotExists("meta_keywords", "аксессуары, чехлы, защитные стекла", "Meta ключевые слова");
    }

    private void createIfNotExists(String key, String value, String description) {
        if (!settingsRepository.existsByKey(key)) {
            SettingsEntity entity = new SettingsEntity();
            entity.setKey(key);
            entity.setValue(value);
            entity.setDescription(description);
            settingsRepository.save(entity);
        }
    }

    public SettingsDto getSettings() {
        SettingsDto dto = new SettingsDto();
        dto.setSiteName(getValue("site_name"));
        dto.setSiteEmail(getValue("site_email"));
        dto.setSitePhone(getValue("site_phone"));
        dto.setSiteAddress(getValue("site_address"));
        dto.setWorkingHours(getValue("working_hours"));
        dto.setVkUrl(getValue("vk_url"));
        dto.setTelegramUrl(getValue("telegram_url"));
        dto.setWhatsappUrl(getValue("whatsapp_url"));
        dto.setMetaDescription(getValue("meta_description"));
        dto.setMetaKeywords(getValue("meta_keywords"));
        return dto;
    }

    private String getValue(String key) {
        return settingsRepository.findByKey(key)
                .map(SettingsEntity::getValue)
                .orElse("");
    }

    @Transactional
    public void updateSettings(SettingsDto dto) {
        updateValue("site_name", dto.getSiteName());
        updateValue("site_email", dto.getSiteEmail());
        updateValue("site_phone", dto.getSitePhone());
        updateValue("site_address", dto.getSiteAddress());
        updateValue("working_hours", dto.getWorkingHours());
        updateValue("vk_url", dto.getVkUrl());
        updateValue("telegram_url", dto.getTelegramUrl());
        updateValue("whatsapp_url", dto.getWhatsappUrl());
        updateValue("meta_description", dto.getMetaDescription());
        updateValue("meta_keywords", dto.getMetaKeywords());
    }

    private void updateValue(String key, String value) {
        if (value != null) {
            SettingsEntity entity = settingsRepository.findByKey(key)
                    .orElse(new SettingsEntity());
            entity.setKey(key);
            entity.setValue(value);
            settingsRepository.save(entity);
        }
    }
}