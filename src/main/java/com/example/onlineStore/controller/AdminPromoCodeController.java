package com.example.onlineStore.controller;

import com.example.onlineStore.dto.PromoCodeDto;
import com.example.onlineStore.entity.PromoCode;
import com.example.onlineStore.mapper.PromoCodeMapper;
import com.example.onlineStore.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/promocodes")
@RequiredArgsConstructor
public class AdminPromoCodeController {

    private final PromoCodeService promoCodeService;
    private final PromoCodeMapper promoCodeMapper;

    @GetMapping
    public String listPromoCodes(Model model) {
        model.addAttribute("promoCodes", promoCodeMapper.toDtoList(promoCodeService.getAllPromoCodes()));
        model.addAttribute("content", "pages/admin/promocodes/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("promoCode", new PromoCodeDto());
        model.addAttribute("content", "pages/admin/promocodes/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createPromoCode(@ModelAttribute PromoCodeDto promoCodeDto,
                                  RedirectAttributes redirectAttributes) {
        try {
            PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDto);
            promoCodeService.createPromoCode(promoCode);
            redirectAttributes.addFlashAttribute("success", "Промокод создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании промокода");
        }
        return "redirect:/admin/promocodes";
    }

    @PostMapping("/delete/{id}")
    public String deletePromoCode(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            promoCodeService.deletePromoCode(id);
            redirectAttributes.addFlashAttribute("success", "Промокод удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении промокода");
        }
        return "redirect:/admin/promocodes";
    }
}