package com.example.onlineStore.controller;

import com.example.onlineStore.dto.OrderRequest;
import com.example.onlineStore.entity.Order;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.mapper.OrderMapper;
import com.example.onlineStore.service.CartService;
import com.example.onlineStore.service.OrderService;
import com.example.onlineStore.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        String sessionId = cartService.getOrCreateSessionId(session);
        User user = getUserFromDetails(userDetails);

        int cartSize = cartService.getCartItemsCount(sessionId, user);
        if (cartSize == 0) {
            return "redirect:/cart";
        }

        OrderRequest orderRequest = new OrderRequest();
        if (user != null) {
            orderRequest.setFullName(user.getFullName());
            orderRequest.setEmail(user.getEmail());
            orderRequest.setPhone(user.getPhone());
        }

        model.addAttribute("orderRequest", orderRequest);
        model.addAttribute("total", cartService.getCartTotal(sessionId, user));
        model.addAttribute("content", "pages/user/order/checkout :: content");

        return "layouts/main";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute OrderRequest orderRequest,
                              BindingResult bindingResult,
                              HttpSession session,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {

        if (bindingResult.hasErrors()) {
            String sessionId = cartService.getOrCreateSessionId(session);
            User user = getUserFromDetails(userDetails);
            model.addAttribute("total", cartService.getCartTotal(sessionId, user));
            model.addAttribute("content", "pages/user/order/checkout :: content");
            return "layouts/main";
        }

        try {
            String sessionId = cartService.getOrCreateSessionId(session);
            User user = getUserFromDetails(userDetails);

            Order order = orderService.createOrder(
                    user, sessionId,
                    orderRequest.getFullName(),
                    orderRequest.getEmail(),
                    orderRequest.getPhone(),
                    orderRequest.getAddress(),
                    orderRequest.getComment(),
                    orderRequest.getPaymentMethod()
            );

            return "redirect:/order/success/" + order.getOrderNumber();

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("orderRequest", orderRequest);
            String sessionId = cartService.getOrCreateSessionId(session);
            User user = getUserFromDetails(userDetails);
            model.addAttribute("total", cartService.getCartTotal(sessionId, user));
            model.addAttribute("content", "pages/user/order/checkout :: content");
            return "layouts/main";
        }
    }

    @GetMapping("/success/{orderNumber}")
    public String success(@PathVariable String orderNumber, Model model) {
        Order order = orderService.getOrderByNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        model.addAttribute("order", orderMapper.toDto(order));
        model.addAttribute("content", "pages/user/order/success :: content");

        return "layouts/main";
    }

    @GetMapping("/my-orders")
    public String myOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = getUserFromDetails(userDetails);

        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orderMapper.toDtoList(orders));
        model.addAttribute("content", "pages/user/order/my-orders :: content");

        return "layouts/main";
    }

    @GetMapping("/{orderNumber}")
    public String orderDetails(@PathVariable String orderNumber,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        Order order = orderService.getOrderByNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        User user = getUserFromDetails(userDetails);

        if (user != null && order.getUser() != null && !order.getUser().getId().equals(user.getId())) {
            return "redirect:/order/my-orders";
        }

        model.addAttribute("order", orderMapper.toDto(order));
        model.addAttribute("content", "pages/user/order/details :: content");

        return "layouts/main";
    }

    private User getUserFromDetails(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userService.getUserByEmail(userDetails.getUsername()).orElse(null);
    }
}