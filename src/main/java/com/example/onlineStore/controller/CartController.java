package com.example.onlineStore.controller;

import com.example.onlineStore.dto.CartItemDto;
import com.example.onlineStore.entity.CartItem;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.mapper.CartItemMapper;
import com.example.onlineStore.service.CartService;
import com.example.onlineStore.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final CartItemMapper cartItemMapper;

    // Просмотр корзины
    @GetMapping
    public String cart(HttpSession session, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        String sessionId = cartService.getOrCreateSessionId(session);
        User user = getUserFromDetails(userDetails);

        List<CartItem> cartItems = cartService.getCartItems(sessionId, user);
        List<CartItemDto> cartItemDtos = cartItemMapper.toDtoList(cartItems);

        BigDecimal total = cartService.getCartTotal(sessionId, user);
        int itemsCount = cartService.getCartItemsCount(sessionId, user);

        model.addAttribute("cartItems", cartItemDtos);
        model.addAttribute("total", total);
        model.addAttribute("itemsCount", itemsCount);

        return "cart/index";
    }

    // Добавить в корзину
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            @AuthenticationPrincipal UserDetails userDetails) {
        String sessionId = cartService.getOrCreateSessionId(session);
        User user = getUserFromDetails(userDetails);

        cartService.addToCart(productId, quantity, sessionId, user);

        return "redirect:/cart";
    }

    // Обновить количество
    @PostMapping("/update/{itemId}")
    public String updateQuantity(@PathVariable Long itemId,
                                 @RequestParam int quantity,
                                 HttpSession session,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        String sessionId = cartService.getOrCreateSessionId(session);
        User user = getUserFromDetails(userDetails);

        cartService.updateQuantity(itemId, quantity, user, sessionId);

        return "redirect:/cart";
    }

    // Удалить из корзины
    @PostMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable Long itemId,
                                 HttpSession session,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        String sessionId = cartService.getOrCreateSessionId(session);
        User user = getUserFromDetails(userDetails);

        cartService.removeFromCart(itemId, user, sessionId);

        return "redirect:/cart";
    }

    // Очистить корзину
    @PostMapping("/clear")
    public String clearCart(HttpSession session, @AuthenticationPrincipal UserDetails userDetails) {
        String sessionId = cartService.getOrCreateSessionId(session);
        User user = getUserFromDetails(userDetails);

        cartService.clearCart(sessionId, user);

        return "redirect:/cart";
    }

    // Получение пользователя из UserDetails
    private User getUserFromDetails(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userService.getUserByEmail(userDetails.getUsername()).orElse(null);
    }
}