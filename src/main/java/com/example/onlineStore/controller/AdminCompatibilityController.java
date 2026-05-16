package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CompatibilityDto;
import com.example.onlineStore.entity.Compatibility;
import com.example.onlineStore.mapper.CompatibilityMapper;
import com.example.onlineStore.service.CompatibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/compatibilities")
@RequiredArgsConstructor
public class AdminCompatibilityController {

    private final CompatibilityService compatibilityService;
    private final CompatibilityMapper compatibilityMapper;

    @GetMapping
    public String listCompatibilities(Model model) {
        model.addAttribute("compatibilities", compatibilityMapper.toDtoList(compatibilityService.getAllCompatibilities()));
        model.addAttribute("content", "pages/admin/compatibilities/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("compatibility", new CompatibilityDto());
        model.addAttribute("content", "pages/admin/compatibilities/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createCompatibility(@ModelAttribute CompatibilityDto compatibilityDto,
                                      RedirectAttributes redirectAttributes) {
        try {
            Compatibility compatibility = compatibilityMapper.toEntity(compatibilityDto);
            compatibilityService.createCompatibility(compatibility);
            redirectAttributes.addFlashAttribute("success", "Совместимость создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании совместимости");
        }
        return "redirect:/admin/compatibilities";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Compatibility compatibility = compatibilityService.getCompatibilityById(id)
                .orElseThrow(() -> new RuntimeException("Совместимость не найдена"));
        model.addAttribute("compatibility", compatibilityMapper.toDto(compatibility));
        model.addAttribute("content", "pages/admin/compatibilities/form :: content");
        return "layouts/main";
    }

    @PostMapping("/update/{id}")
    public String updateCompatibility(@PathVariable Long id,
                                      @ModelAttribute CompatibilityDto compatibilityDto,
                                      RedirectAttributes redirectAttributes) {
        try {
            Compatibility compatibility = compatibilityMapper.toEntity(compatibilityDto);
            compatibilityService.updateCompatibility(id, compatibility);
            redirectAttributes.addFlashAttribute("success", "Совместимость обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении совместимости");
        }
        return "redirect:/admin/compatibilities";
    }

    @PostMapping("/delete/{id}")
    public String deleteCompatibility(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            compatibilityService.deleteCompatibility(id);
            redirectAttributes.addFlashAttribute("success", "Совместимость удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении совместимости");
        }
        return "redirect:/admin/compatibilities";
    }
}