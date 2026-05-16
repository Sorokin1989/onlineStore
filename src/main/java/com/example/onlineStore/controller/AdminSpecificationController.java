package com.example.onlineStore.controller;

import com.example.onlineStore.dto.SpecificationDto;
import com.example.onlineStore.entity.Specification;
import com.example.onlineStore.mapper.SpecificationMapper;
import com.example.onlineStore.service.SpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/specifications")
@RequiredArgsConstructor
public class AdminSpecificationController {

    private final SpecificationService specificationService;
    private final SpecificationMapper specificationMapper;

    @GetMapping
    public String listSpecifications(Model model) {
        model.addAttribute("specifications", specificationMapper.toDtoList(specificationService.getAllSpecifications()));
        model.addAttribute("content", "pages/admin/specifications/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("specification", new SpecificationDto());
        model.addAttribute("content", "pages/admin/specifications/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createSpecification(@ModelAttribute SpecificationDto specificationDto,
                                      RedirectAttributes redirectAttributes) {
        try {
            Specification specification = specificationMapper.toEntity(specificationDto);
            specificationService.createSpecification(specification);
            redirectAttributes.addFlashAttribute("success", "Характеристика создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании характеристики");
        }
        return "redirect:/admin/specifications";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Specification specification = specificationService.getSpecificationById(id)
                .orElseThrow(() -> new RuntimeException("Характеристика не найдена"));
        model.addAttribute("specification", specificationMapper.toDto(specification));
        model.addAttribute("content", "pages/admin/specifications/form :: content");
        return "layouts/main";
    }

    @PostMapping("/update/{id}")
    public String updateSpecification(@PathVariable Long id,
                                      @ModelAttribute SpecificationDto specificationDto,
                                      RedirectAttributes redirectAttributes) {
        try {
            Specification specification = specificationMapper.toEntity(specificationDto);
            specificationService.updateSpecification(id, specification);
            redirectAttributes.addFlashAttribute("success", "Характеристика обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении характеристики");
        }
        return "redirect:/admin/specifications";
    }

    @PostMapping("/delete/{id}")
    public String deleteSpecification(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            specificationService.deleteSpecification(id);
            redirectAttributes.addFlashAttribute("success", "Характеристика удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении характеристики");
        }
        return "redirect:/admin/specifications";
    }
}