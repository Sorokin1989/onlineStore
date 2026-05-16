package com.example.onlineStore.controller;

import com.example.onlineStore.dto.UserRegistrationRequest;
import com.example.onlineStore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Страница логина
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "registered", required = false) String registered,
                        Model model) {

        if (error != null) {
            model.addAttribute("error", "Неверный email или пароль");
        }

        if (logout != null) {
            model.addAttribute("message", "Вы вышли из аккаунта");
        }

        if (registered != null) {
            model.addAttribute("message", "Регистрация успешна! Войдите в аккаунт.");
        }
        model.addAttribute("content","pages/user/login :: content");

        return "layouts/main";
    }

    // Страница регистрации
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserRegistrationRequest());
        model.addAttribute("content", "pages/user/register :: content");
        return "layouts/main";
    }

    // Обработка регистрации
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegistrationRequest request,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("page", "pages/user/register");
            return "layouts/main";
        }

        // Проверка, что пароли совпадают
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Пароли не совпадают");
            return "layouts/main";
        }

        try {
            userService.registerUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getFullName(),
                    request.getPhone()
            );
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "layouts/main";
        }
    }
}