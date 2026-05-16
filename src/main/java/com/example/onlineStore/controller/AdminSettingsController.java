package com.example.onlineStore.controller.admin;

import com.example.onlineStore.dto.SettingsDto;
import com.example.onlineStore.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/settings")
@RequiredArgsConstructor
public class AdminSettingsController {

    private final SettingsService settingsService;

    @GetMapping
    public String settings(Model model) {
        model.addAttribute("settings", settingsService.getSettings());
        model.addAttribute("content", "pages/admin/settings/index :: content");
        return "layouts/main";
    }

    @PostMapping("/update")
    public String updateSettings(@ModelAttribute SettingsDto settingsDto,
                                 RedirectAttributes redirectAttributes) {
        try {
            settingsService.updateSettings(settingsDto);
            redirectAttributes.addFlashAttribute("success", "Настройки сохранены");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при сохранении настроек");
        }
        return "redirect:/admin/settings";
    }
}