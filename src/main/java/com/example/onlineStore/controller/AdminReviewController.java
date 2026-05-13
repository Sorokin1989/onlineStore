package com.example.onlineStore.controller;

import com.example.onlineStore.entity.Review;
import com.example.onlineStore.mapper.ReviewMapper;
import com.example.onlineStore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    // Список отзывов
    @GetMapping
    public String listReviews(@RequestParam(required = false) String status, Model model) {
        java.util.List<Review> reviews;
        if ("PENDING".equals(status)) {
            reviews = reviewService.getPendingReviews();
            model.addAttribute("currentStatus", "PENDING");
        } else {
            reviews = reviewService.getAllReviews();
        }

        model.addAttribute("reviews", reviewMapper.toDtoList(reviews));
        return "admin/reviews/list";
    }

    // Одобрить отзыв
    @PostMapping("/{id}/approve")
    public String approveReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.approveReview(id);
            redirectAttributes.addFlashAttribute("success", "Отзыв одобрен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при одобрении отзыва");
        }
        return "redirect:/admin/reviews";
    }

    // Отклонить отзыв
    @PostMapping("/{id}/reject")
    public String rejectReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.rejectReview(id);
            redirectAttributes.addFlashAttribute("success", "Отзыв отклонён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при отклонении отзыва");
        }
        return "redirect:/admin/reviews";
    }

    // Удалить отзыв
    @PostMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Отзыв удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении отзыва");
        }
        return "redirect:/admin/reviews";
    }
}