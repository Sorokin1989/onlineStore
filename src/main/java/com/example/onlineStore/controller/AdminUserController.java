package com.example.onlineStore.controller;

import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.UserRole;
import com.example.onlineStore.mapper.UserMapper;
import com.example.onlineStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    // Список пользователей
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userMapper.toDtoList(userService.getAllUsers()));
        model.addAttribute("roles", UserRole.values());
        return "admin/users/list";
    }

    // Детали пользователя
    @GetMapping("/{id}")
    public String userDetails(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        model.addAttribute("user", userMapper.toDto(user));
        model.addAttribute("roles", UserRole.values());
        return "admin/users/details";
    }

    // Изменение роли пользователя
    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam String role,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserRole(id, role);
            redirectAttributes.addFlashAttribute("success", "Роль пользователя изменена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при изменении роли");
        }
        return "redirect:/admin/users/" + id;
    }

    // Блокировка/разблокировка пользователя
    @PostMapping("/{id}/toggle-block")
    public String toggleBlock(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserBlock(id);
            redirectAttributes.addFlashAttribute("success", "Статус пользователя изменён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при изменении статуса");
        }
        return "redirect:/admin/users/" + id;
    }
}