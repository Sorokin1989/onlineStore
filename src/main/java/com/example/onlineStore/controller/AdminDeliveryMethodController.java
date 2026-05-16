package com.example.onlineStore.controller;

import com.example.onlineStore.dto.DeliveryMethodDto;
import com.example.onlineStore.entity.DeliveryMethod;
import com.example.onlineStore.mapper.DeliveryMethodMapper;

import com.example.onlineStore.service.DeliveryMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/delivery-methods")
@RequiredArgsConstructor
public class AdminDeliveryMethodController {

    private final DeliveryMethodService deliveryMethodService;
    private final DeliveryMethodMapper deliveryMethodMapper;

    @GetMapping
    public String listDeliveryMethods(Model model) {
        model.addAttribute("deliveryMethods", deliveryMethodMapper.toDtoList(deliveryMethodService.getAllDeliveryMethods()));
        model.addAttribute("content", "pages/admin/delivery-methods/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("deliveryMethod", new DeliveryMethodDto());
        model.addAttribute("content", "pages/admin/delivery-methods/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createDeliveryMethod(@ModelAttribute DeliveryMethodDto deliveryMethodDto,
                                       RedirectAttributes redirectAttributes) {
        try {
            DeliveryMethod deliveryMethod = deliveryMethodMapper.toEntity(deliveryMethodDto);
            deliveryMethodService.createDeliveryMethod(deliveryMethod);
            redirectAttributes.addFlashAttribute("success", "Способ доставки создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании способа доставки");
        }
        return "redirect:/admin/delivery-methods";
    }

    @PostMapping("/delete/{id}")
    public String deleteDeliveryMethod(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            deliveryMethodService.deleteDeliveryMethod(id);
            redirectAttributes.addFlashAttribute("success", "Способ доставки удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении способа доставки");
        }
        return "redirect:/admin/delivery-methods";
    }
}