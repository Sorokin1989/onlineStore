package com.example.onlineStore.controller;

import com.example.onlineStore.entity.Order;
import com.example.onlineStore.enums.OrderStatus;
import com.example.onlineStore.mapper.OrderMapper;
import com.example.onlineStore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    // Список заказов
    @GetMapping
    public String listOrders(@RequestParam(required = false) String status, Model model) {
        java.util.List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderService.getOrdersByStatus(status);
            model.addAttribute("currentStatus", status);
        } else {
            orders = orderService.getAllOrders();
        }

        model.addAttribute("orders", orderMapper.toDtoList(orders));
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders/list";
    }

    // Детали заказа
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        model.addAttribute("order", orderMapper.toDto(order));
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/orders/details";
    }

    // Обновление статуса заказа
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Статус заказа обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении статуса");
        }
        return "redirect:/admin/orders/" + id;
    }
}