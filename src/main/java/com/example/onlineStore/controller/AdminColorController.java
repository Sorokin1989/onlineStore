package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ColorDto;
import com.example.onlineStore.entity.Color;
import com.example.onlineStore.mapper.ColorMapper;
import com.example.onlineStore.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/colors")
@RequiredArgsConstructor
public class AdminColorController {

    private final ColorService colorService;
    private final ColorMapper colorMapper;

    @GetMapping
    public String listColors(Model model) {
        model.addAttribute("colors", colorMapper.toDtoList(colorService.getAllColors()));
        model.addAttribute("content", "pages/admin/colors/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("color", new ColorDto());
        model.addAttribute("content", "pages/admin/colors/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createColor(@ModelAttribute ColorDto colorDto,
                              RedirectAttributes redirectAttributes) {
        try {
            Color color = colorMapper.toEntity(colorDto);
            colorService.createColor(color);
            redirectAttributes.addFlashAttribute("success", "Цвет создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании цвета");
        }
        return "redirect:/admin/colors";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Color color = colorService.getColorById(id)
                .orElseThrow(() -> new RuntimeException("Цвет не найден"));
        model.addAttribute("color", colorMapper.toDto(color));
        model.addAttribute("content", "pages/admin/colors/form :: content");
        return "layouts/main";
    }

    @PostMapping("/update/{id}")
    public String updateColor(@PathVariable Long id,
                              @ModelAttribute ColorDto colorDto,
                              RedirectAttributes redirectAttributes) {
        try {
            Color color = colorMapper.toEntity(colorDto);
            colorService.updateColor(id, color);
            redirectAttributes.addFlashAttribute("success", "Цвет обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении цвета");
        }
        return "redirect:/admin/colors";
    }

    @PostMapping("/delete/{id}")
    public String deleteColor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            colorService.deleteColor(id);
            redirectAttributes.addFlashAttribute("success", "Цвет удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении цвета");
        }
        return "redirect:/admin/colors";
    }
}