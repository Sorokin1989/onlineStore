package com.example.onlineStore.controller;

import com.example.onlineStore.dto.WarrantyDto;
import com.example.onlineStore.entity.Warranty;
import com.example.onlineStore.mapper.WarrantyMapper;
import com.example.onlineStore.service.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/warranties")
@RequiredArgsConstructor
public class AdminWarrantyController {

    private final WarrantyService warrantyService;
    private final WarrantyMapper warrantyMapper;

    @GetMapping
    public String listWarranties(Model model) {
        model.addAttribute("warranties", warrantyMapper.toDtoList(warrantyService.getAllWarranties()));
        model.addAttribute("content", "pages/admin/warranties/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("warranty", new WarrantyDto());
        model.addAttribute("content", "pages/admin/warranties/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createWarranty(@ModelAttribute WarrantyDto warrantyDto,
                                 RedirectAttributes redirectAttributes) {
        try {
            Warranty warranty = warrantyMapper.toEntity(warrantyDto);
            warrantyService.createWarranty(warranty);
            redirectAttributes.addFlashAttribute("success", "Гарантия создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании гарантии");
        }
        return "redirect:/admin/warranties";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Warranty warranty = warrantyService.getWarrantyById(id)
                .orElseThrow(() -> new RuntimeException("Гарантия не найдена"));
        model.addAttribute("warranty", warrantyMapper.toDto(warranty));
        model.addAttribute("content", "pages/admin/warranties/form :: content");
        return "layouts/main";
    }

    @PostMapping("/update/{id}")
    public String updateWarranty(@PathVariable Long id,
                                 @ModelAttribute WarrantyDto warrantyDto,
                                 RedirectAttributes redirectAttributes) {
        try {
            Warranty warranty = warrantyMapper.toEntity(warrantyDto);
            warrantyService.updateWarranty(id, warranty);
            redirectAttributes.addFlashAttribute("success", "Гарантия обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении гарантии");
        }
        return "redirect:/admin/warranties";
    }

    @PostMapping("/delete/{id}")
    public String deleteWarranty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            warrantyService.deleteWarranty(id);
            redirectAttributes.addFlashAttribute("success", "Гарантия удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении гарантии");
        }
        return "redirect:/admin/warranties";
    }
}