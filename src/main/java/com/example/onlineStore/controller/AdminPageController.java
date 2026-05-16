package com.example.onlineStore.controller;

import com.example.onlineStore.dto.PageDto;
import com.example.onlineStore.entity.Page;
import com.example.onlineStore.mapper.PageMapper;
import com.example.onlineStore.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pages")
@RequiredArgsConstructor
public class AdminPageController {

    private final PageService pageService;
    private final PageMapper pageMapper;

    @GetMapping
    public String listPages(Model model) {
        model.addAttribute("pages", pageMapper.toDtoList(pageService.getAllPages()));
        model.addAttribute("content", "pages/admin/pages/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("page", new PageDto());
        model.addAttribute("content", "pages/admin/pages/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createPage(@ModelAttribute PageDto pageDto,
                             RedirectAttributes redirectAttributes) {
        try {
            Page page = pageMapper.toEntity(pageDto);
            pageService.createPage(page);
            redirectAttributes.addFlashAttribute("success", "Страница создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании страницы");
        }
        return "redirect:/admin/pages";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Page page = pageService.getPageById(id)
                .orElseThrow(() -> new RuntimeException("Страница не найдена"));
        model.addAttribute("page", pageMapper.toDto(page));
        model.addAttribute("content", "pages/admin/pages/form :: content");
        return "layouts/main";
    }

    @PostMapping("/update/{id}")
    public String updatePage(@PathVariable Long id,
                             @ModelAttribute PageDto pageDto,
                             RedirectAttributes redirectAttributes) {
        try {
            Page page = pageMapper.toEntity(pageDto);
            pageService.updatePage(id, page);
            redirectAttributes.addFlashAttribute("success", "Страница обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении страницы");
        }
        return "redirect:/admin/pages";
    }

    @PostMapping("/delete/{id}")
    public String deletePage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pageService.deletePage(id);
            redirectAttributes.addFlashAttribute("success", "Страница удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении страницы");
        }
        return "redirect:/admin/pages";
    }
}