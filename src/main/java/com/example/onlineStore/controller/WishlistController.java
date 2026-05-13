package com.example.onlineStore.controller;

import com.example.onlineStore.dto.ProductDto;
import com.example.onlineStore.entity.Favorite;
import com.example.onlineStore.entity.Product;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.mapper.ProductMapper;
import com.example.onlineStore.service.FavoriteService;
import com.example.onlineStore.service.ProductService;
import com.example.onlineStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final FavoriteService favoriteService;
    private final ProductService productService;
    private final UserService userService;
    private final ProductMapper productMapper;

    // Избранное пользователя
    @GetMapping
    public String wishlist(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
        List<Product> products = favorites.stream()
                .map(Favorite::getProduct)
                .collect(Collectors.toList());

        model.addAttribute("products", productMapper.toDtoList(products));
        model.addAttribute("count", favorites.size());

        return "wishlist/index";
    }

    // Добавить в избранное
    @PostMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        favoriteService.addToFavorites(user, product);

        return "redirect:/wishlist";
    }

    // Удалить из избранного
    @PostMapping("/remove/{productId}")
    public String removeFromWishlist(@PathVariable Long productId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        favoriteService.removeFromFavorites(user, productId);

        return "redirect:/wishlist";
    }
}