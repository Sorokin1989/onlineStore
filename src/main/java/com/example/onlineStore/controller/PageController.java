package com.example.onlineStore.controller;

import com.example.onlineStore.entity.Page;
import com.example.onlineStore.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    // Статическая страница (о нас, доставка, контакты)
    @GetMapping("/page/{slug}")
    public String page(@PathVariable String slug, Model model) {
        Page page = pageService.getPageBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Страница не найдена"));

        model.addAttribute("page", page);
        return "page";
    }

    // Контакты (если отдельно)
    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    // О нас
    @GetMapping("/about")
    public String about(Model model) {
        Page page = pageService.getPageBySlug("about")
                .orElse(null);
        model.addAttribute("page", page);
        return "page";
    }

    // Доставка и оплата
    @GetMapping("/delivery")
    public String delivery(Model model) {
        Page page = pageService.getPageBySlug("delivery")
                .orElse(null);
        model.addAttribute("page", page);
        return "page";
    }
}