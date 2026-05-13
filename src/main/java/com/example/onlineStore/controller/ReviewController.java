package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ReviewRequest;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.Review;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.mapper.ReviewMapper;
import com.example.onlineStore.service.OrderService;
import com.example.onlineStore.service.ProductService;
import com.example.onlineStore.service.ReviewService;
import com.example.onlineStore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final ReviewMapper reviewMapper;

    // Страница добавления отзыва
    @GetMapping("/add/{productId}")
    public String reviewForm(@PathVariable Long productId,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        // Проверка, покупал ли пользователь этот товар
        boolean canReview = orderService.hasUserBoughtProduct(user, product);

        if (!canReview) {
            return "redirect:/product/" + product.getSlug() + "?error=not_purchased";
        }

        model.addAttribute("product", product);
        model.addAttribute("review", new ReviewRequest());

        return "reviews/form";
    }

    // Сохранение отзыва
    @PostMapping("/add/{productId}")
    public String addReview(@PathVariable Long productId,
                            @Valid @ModelAttribute ReviewRequest reviewRequest,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productService.getProductById(productId).orElse(null));
            return "reviews/form";
        }

        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        reviewService.createReview(product, user, reviewRequest.getRating(), reviewRequest.getComment());

        return "redirect:/product/" + product.getSlug() + "?review_added";
    }
}