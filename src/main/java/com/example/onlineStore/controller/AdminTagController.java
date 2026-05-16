package com.example.onlineStore.controller;

import com.example.onlineStore.dto.TagDto;
import com.example.onlineStore.entity.Tag;
import com.example.onlineStore.mapper.TagMapper;
import com.example.onlineStore.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public String listTags(Model model) {
        model.addAttribute("tags", tagMapper.toDtoList(tagService.getAllTags()));
        model.addAttribute("content", "pages/admin/tags/list :: content");
        return "layouts/main";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("tag", new TagDto());
        model.addAttribute("content", "pages/admin/tags/form :: content");
        return "layouts/main";
    }

    @PostMapping("/create")
    public String createTag(@ModelAttribute TagDto tagDto,
                            RedirectAttributes redirectAttributes) {
        try {
            Tag tag = tagMapper.toEntity(tagDto);
            tagService.createTag(tag);
            redirectAttributes.addFlashAttribute("success", "Тег создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании тега");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Tag tag = tagService.getTagById(id)
                .orElseThrow(() -> new RuntimeException("Тег не найден"));
        model.addAttribute("tag", tagMapper.toDto(tag));
        model.addAttribute("content", "pages/admin/tags/form :: content");
        return "layouts/main";
    }

    @PostMapping("/update/{id}")
    public String updateTag(@PathVariable Long id,
                            @ModelAttribute TagDto tagDto,
                            RedirectAttributes redirectAttributes) {
        try {
            Tag tag = tagMapper.toEntity(tagDto);
            tagService.updateTag(id, tag);
            redirectAttributes.addFlashAttribute("success", "Тег обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении тега");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tagService.deleteTag(id);
            redirectAttributes.addFlashAttribute("success", "Тег удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении тега");
        }
        return "redirect:/admin/tags";
    }
}