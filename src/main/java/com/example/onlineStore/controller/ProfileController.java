package com.example.onlineStore.controller;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.mapper.UserMapper;
import com.example.onlineStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final UserMapper userMapper;

    // Страница профиля
    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        model.addAttribute("user", userMapper.toDto(user));
        return "profile/index";
    }

    // Форма редактирования профиля
    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        model.addAttribute("user", userMapper.toDto(user));
        return "profile/edit";
    }

    // Обновление профиля
    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String fullName,
                                @RequestParam String phone) {
        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        userService.updateProfile(user.getId(), fullName, phone);
        return "redirect:/profile?updated";
    }

    // Форма смены пароля
    @GetMapping("/change-password")
    public String changePasswordForm() {
        return "profile/change-password";
    }

    // Смена пароля
    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "profile/change-password";
        }

        if (newPassword.length() < 6) {
            model.addAttribute("error", "Пароль должен содержать минимум 6 символов");
            return "profile/change-password";
        }

        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        try {
            userService.changePassword(user.getId(), oldPassword, newPassword);
            return "redirect:/profile?password-changed";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "profile/change-password";
        }
    }
}