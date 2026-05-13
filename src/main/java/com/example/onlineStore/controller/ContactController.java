package com.example.onlineStore.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam String name,
                              @RequestParam String email,
                              @RequestParam(required = false) String phone,
                              @RequestParam String message,
                              Model model) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(adminEmail);
            mail.setSubject("Новое сообщение с сайта от " + name);
            mail.setText("Имя: " + name + "\n"
                    + "Email: " + email + "\n"
                    + (phone != null ? "Телефон: " + phone + "\n" : "")
                    + "\nСообщение:\n" + message);
            mail.setReplyTo(email);

            mailSender.send(mail);

            model.addAttribute("success", "Сообщение отправлено! Мы свяжемся с вами в ближайшее время.");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при отправке. Попробуйте позже или напишите нам напрямую на email.");
        }

        return "contacts";
    }
}