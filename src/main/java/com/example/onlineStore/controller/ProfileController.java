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

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = getUserFromDetails(userDetails);
        model.addAttribute("user", userMapper.toDto(user));
        model.addAttribute("content", "pages/user/profile/index :: content");
        return "layouts/main";
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = getUserFromDetails(userDetails);
        model.addAttribute("user", userMapper.toDto(user));
        model.addAttribute("content", "pages/user/profile/edit :: content");
        return "layouts/main";
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String fullName,
                                @RequestParam String phone) {
        User user = getUserFromDetails(userDetails);
        userService.updateProfile(user.getId(), fullName, phone);
        return "redirect:/profile?updated";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("content", "pages/user/profile/change-password :: content");
        return "layouts/main";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("content", "pages/user/profile/change-password :: content");
            return "layouts/main";
        }

        if (newPassword.length() < 6) {
            model.addAttribute("error", "Пароль должен содержать минимум 6 символов");
            model.addAttribute("content", "pages/user/profile/change-password :: content");
            return "layouts/main";
        }

        User user = getUserFromDetails(userDetails);

        try {
            userService.changePassword(user.getId(), oldPassword, newPassword);
            return "redirect:/profile?password-changed";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("content", "pages/user/profile/change-password :: content");
            return "layouts/main";
        }
    }

    private User getUserFromDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Пользователь не авторизован");
        }
        return userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}