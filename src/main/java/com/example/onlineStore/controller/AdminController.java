package com.example.onlineStore.controller;

import com.example.onlineStore.enums.OrderStatus;
import com.example.onlineStore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", productService.getAllProducts().size());
        model.addAttribute("totalCategories", categoryService.getAllCategories().size());
        model.addAttribute("totalOrders", orderService.getAllOrders().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("pendingReviews", reviewService.getPendingReviews().size());
        model.addAttribute("newOrders", orderService.getOrdersByStatus(OrderStatus.NEW).size()); // Исправлено: OrderStatus.NEW

        // Добавляем settings для совместимости с layout
        model.addAttribute("settings", new Object());

        // ИСПРАВЛЕНО: указываем на правильный фрагмент дашборда
        model.addAttribute("content", "pages/admin/dashboard :: content");

        return "layouts/main";
    }
}