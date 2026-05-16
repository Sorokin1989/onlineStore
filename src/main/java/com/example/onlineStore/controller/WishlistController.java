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

    @GetMapping
    public String wishlist(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = getUserFromDetails(userDetails);

        List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
        List<Product> products = favorites.stream()
                .map(Favorite::getProduct)
                .collect(Collectors.toList());

        model.addAttribute("products", productMapper.toDtoList(products));
        model.addAttribute("count", favorites.size());
        model.addAttribute("content", "pages/user/wishlist/index :: content");

        return "layouts/main";
    }

    @PostMapping("/add/{productId}")
    public String addToWishlist(@PathVariable Long productId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromDetails(userDetails);
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        favoriteService.addToFavorites(user, product);
        return "redirect:/wishlist";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromWishlist(@PathVariable Long productId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        User user = getUserFromDetails(userDetails);
        favoriteService.removeFromFavorites(user, productId);
        return "redirect:/wishlist";
    }

    private User getUserFromDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Пользователь не авторизован");
        }
        return userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}